package x590.jdecompiler.attribute;

import java.util.Arrays;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.ModuleConstant;
import x590.jdecompiler.constpool.PackageConstant;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.ModuleEntryModifiers;
import x590.jdecompiler.modifiers.ModuleModifiers;
import x590.jdecompiler.modifiers.ModuleRequirementModifiers;
import x590.util.ArrayUtil;
import x590.util.annotation.Nullable;

public class ModuleAttribute extends Attribute implements StringWritable<ClassInfo> {
	
	private final ModuleConstant module;
	private final ModuleModifiers modifiers;
	private final @Nullable String version;
	private final IModuleEntry[][] allEntries;
	
	protected ModuleAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		this.module = pool.get(in.readUnsignedShort());
		this.modifiers = ModuleModifiers.read(in);
		this.version = pool.getUtf8StringNullable(in.readUnsignedShort());
		
		this.allEntries = new IModuleEntry[][] {
				in.readArray(RequirementEntry[]::new, () -> new RequirementEntry(in, pool)),
				in.readArray(ExportsEntry[]::new,     () -> new ExportsEntry(in, pool)),
				in.readArray(OpensEntry[]::new,       () -> new OpensEntry(in, pool)),
				in.readArray(ServiceEntry[]::new,     () -> new ServiceEntry(in, pool)),
				in.readArray(ProvidingEntry[]::new,   () -> new ProvidingEntry(in, pool))
		};
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		for(IModuleEntry[] entries : allEntries) {
			for(IModuleEntry entry : entries) {
				entry.addImports(classinfo);
			}
		}
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(modifiers, classinfo).printsp("module").printsp(module, classinfo);
		
		if(Arrays.stream(allEntries).allMatch(entries -> entries.length == 0)) {
			out.write("{}");
			
		} else {
			out.print('{').increaseIndent();
			
			for(IModuleEntry[] entries : allEntries) {
				if(entries.length != 0)
					out.println().printEach(entries, classinfo, entry -> out.println().printIndent().print(entry, classinfo));
			}
			
			out.reduceIndent().println().printIndent().print('}');
		}
	}
	
	
	private interface IModuleEntry extends StringWritable<ClassInfo>, Importable {}
	
	
	private static class RequirementEntry implements IModuleEntry {
		
		private final ModuleConstant module;
		private final ModuleRequirementModifiers modifiers;
		private final @Nullable String version;
		
		private RequirementEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.module = pool.get(in.readUnsignedShort());
			this.modifiers = ModuleRequirementModifiers.read(in);
			this.version = pool.getUtf8StringNullable(in.readUnsignedShort());
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp("requires").print(modifiers, classinfo).print(module, classinfo).write(';');
		}
	}
	
	
	private static abstract class ExportsOrOpensEntry implements IModuleEntry {
		
		private final PackageConstant packageConstant;
		private final ModuleEntryModifiers modifiers;
		private final ModuleConstant[] modules;
		
		private ExportsOrOpensEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.packageConstant = pool.get(in.readUnsignedShort());
			this.modifiers = ModuleEntryModifiers.read(in);
			this.modules = in.readArray(ModuleConstant[]::new, () -> pool.get(in.readUnsignedShort()));
		}
		
		
		protected abstract String getDeclaration();
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp(getDeclaration()).print(modifiers, classinfo).print(packageConstant, classinfo);
			
			if(modules.length != 0) {
				out.printsp(" to").writeAll(modules, classinfo);
			}
			
			out.write(';');
		}
	}
	
	private static class ExportsEntry extends ExportsOrOpensEntry {
		
		private ExportsEntry(ExtendedDataInputStream in, ConstantPool pool) {
			super(in, pool);
		}
		
		@Override
		protected String getDeclaration() {
			return "exports";
		}
	}
	
	private static class OpensEntry extends ExportsOrOpensEntry {
		
		private OpensEntry(ExtendedDataInputStream in, ConstantPool pool) {
			super(in, pool);
		}
		
		@Override
		protected String getDeclaration() {
			return "opens";
		}
	}
	
	
	private static class ServiceEntry implements IModuleEntry {
		
		protected final ClassConstant service;
		
		private ServiceEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.service = pool.get(in.readUnsignedShort());
		}
		
		@Override
		public void addImports(ClassInfo classinfo) {
			service.addImports(classinfo);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp("uses").print(service, classinfo).write(';');
		}
	}
	
	private static class ProvidingEntry extends ServiceEntry {
		
		private final ClassConstant[] providesWith;
		
		private ProvidingEntry(ExtendedDataInputStream in, ConstantPool pool) {
			super(in, pool);
			this.providesWith = in.readArray(ClassConstant[]::new, () -> pool.get(in.readUnsignedShort()));
		}
		
		@Override
		public void addImports(ClassInfo classinfo) {
			super.addImports(classinfo);
			ArrayUtil.forEach(providesWith, serviceImplementation -> serviceImplementation.addImports(classinfo));
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp("provides").printsp(service, classinfo).printsp("with").printAll(providesWith, classinfo, ", ").write(';');
		}
	}
}
