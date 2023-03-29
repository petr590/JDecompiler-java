package x590.jdecompiler.attribute;

import java.util.List;

import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.ModuleConstant;
import x590.jdecompiler.constpool.PackageConstant;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.ModuleEntryModifiers;
import x590.jdecompiler.modifiers.ModuleModifiers;
import x590.jdecompiler.modifiers.ModuleRequirementModifiers;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class ModuleAttribute extends Attribute implements StringifyWritable<ClassInfo> {
	
	private final ModuleConstant module;
	private final ModuleModifiers modifiers;
	private final @Nullable String version;
	private final @Immutable List<@Immutable List<IModuleEntry>> allEntries;
	
	protected ModuleAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		this.module = pool.get(in.readUnsignedShort());
		this.modifiers = ModuleModifiers.read(in);
		this.version = pool.getNullableUtf8String(in.readUnsignedShort());
		
		this.allEntries = List.of(
				in.readImmutableList(() -> new RequirementEntry(in, pool)),
				in.readImmutableList(() -> new ExportsEntry(in, pool)),
				in.readImmutableList(() -> new OpensEntry(in, pool)),
				in.readImmutableList(() -> new ServiceEntry(in, pool)),
				in.readImmutableList(() -> new ProvidingEntry(in, pool))
		);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		allEntries.forEach(entries -> classinfo.addImportsFor(entries));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(modifiers, classinfo).printsp("module").printsp(module, classinfo);
		
		if(allEntries.stream().allMatch(List::isEmpty)) {
			out.write("{}");
			
		} else {
			out.print('{').increaseIndent();
			
			for(List<? extends IModuleEntry> entries : allEntries) {
				if(!entries.isEmpty())
					out.println().printEachUsingFunction(entries, entry -> out.println().printIndent().print(entry, classinfo));
			}
			
			out.reduceIndent().println().printIndent().print('}');
		}
	}
	
	
	private interface IModuleEntry extends StringifyWritable<ClassInfo>, Importable {}
	
	
	private static class RequirementEntry implements IModuleEntry {
		
		private final ModuleConstant module;
		private final ModuleRequirementModifiers modifiers;
		private final @Nullable String version;
		
		private RequirementEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.module = pool.get(in.readUnsignedShort());
			this.modifiers = ModuleRequirementModifiers.read(in);
			this.version = pool.getNullableUtf8String(in.readUnsignedShort());
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp("requires").print(modifiers, classinfo).print(module, classinfo).write(';');
		}
	}
	
	
	private static abstract class ExportsOrOpensEntry implements IModuleEntry {
		
		private final PackageConstant packageConstant;
		private final ModuleEntryModifiers modifiers;
		private final @Immutable List<ModuleConstant> modules;
		
		private ExportsOrOpensEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.packageConstant = pool.get(in.readUnsignedShort());
			this.modifiers = ModuleEntryModifiers.read(in);
			this.modules = in.readImmutableList(() -> pool.get(in.readUnsignedShort()));
		}
		
		
		protected abstract String getDeclaration();
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp(getDeclaration()).print(modifiers, classinfo).print(packageConstant, classinfo);
			
			if(!modules.isEmpty()) {
				out.printsp(" to").printAll(modules, classinfo);
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
		
		private final @Immutable List<ClassConstant> providesWith;
		
		private ProvidingEntry(ExtendedDataInputStream in, ConstantPool pool) {
			super(in, pool);
			this.providesWith = in.readImmutableList(() -> pool.get(in.readUnsignedShort()));
		}
		
		@Override
		public void addImports(ClassInfo classinfo) {
			super.addImports(classinfo);
			classinfo.addImportsFor(providesWith);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.printsp("provides").printsp(service, classinfo).printsp("with").printAll(providesWith, classinfo, ", ").write(';');
		}
	}
}
