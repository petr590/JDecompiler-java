package x590.jdecompiler.experimental.codeanalysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.constpool.MethodrefConstant;
import x590.jdecompiler.instruction.invoke.InvokeInstruction;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.method.JavaMethod;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.testing.DecompilationTesting;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.Logger;

public class CodeAnalysis {
	
	public static void main(String[] args) {
		
		checkPermissions(
				DecompilationTesting.findAllClassPathsAsStreamInPackage("x590/jdecompiler")
						.filter(path -> !path.startsWith("x590/jdecompiler/example"))
						.collect(Collectors.toSet())
		);
		
//		try(InputStream in = ClassLoader.getSystemClassLoader()
//				.getResource("x590/jdecompiler/instruction/arraystore/AAStoreInstruction.class")
//				.openStream()) {
//			
//			var classinfo = JavaClass.read(in).getClassInfo();
//			
//			var method = classinfo.getMethod(
//					MethodDescriptor.of(
//							ClassType.fromDescriptor("x590/jdecompiler/operation/Operation"),
//							ClassType.fromDescriptor("x590/jdecompiler/instruction/arraystore/AAStoreInstruction"),
//							"toOperation",
//							ClassType.fromDescriptor("x590/jdecompiler/context/DecompilationContext")
//					)
//			);
//			
//			method.resolveOverrideAnnotation(classinfo);
//			
//			System.out.println(method.hasOverrideAnnotation());
//			
//		} catch(IOException ex) {
//			ex.printStackTrace();
//		}
	}
	
	private static void checkPermissions(Set<String> classPaths) {
		
		JDecompiler.init(classPaths.toArray(String[]::new));
		
		JDecompiler jdecompiler = JDecompiler.getInstance();
		
		List<JavaClass> classes = new ArrayList<>(jdecompiler.getFiles().size());
		
		var classLoader = ClassLoader.getSystemClassLoader();
		
		var nullStream = new PrintStream(OutputStream.nullOutputStream());
		
		Logger.setOutputAndErrorStream(nullStream, nullStream);
		
		for(String file : jdecompiler.getFiles()) {
			
			try(InputStream in = classLoader.getResource(file).openStream()) {
				
				classes.add(JavaClass.read(in));
				
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		Object2BooleanMap<MethodDescriptor> methodsUsage = new Object2BooleanArrayMap<>();
		
		for(JavaClass clazz : classes) {
			
			var classinfo = clazz.getClassInfo();
			var pool = clazz.getConstPool();
			
			for(JavaMethod method : clazz.getMethods()) {
				
				if(method.getModifiers().isSynthetic())
					continue;
				
				var descriptor = method.getDescriptor();
				
				if(!descriptor.isPlain()) {
					methodsUsage.put(descriptor, true);
					
				} else if(!methodsUsage.getBoolean(descriptor)) {
					
					method.resolveOverrideAnnotation(classinfo);
					methodsUsage.put(descriptor, method.hasOverrideAnnotation());
				}
				
				for(var instruction : method.getDisassemblerContext().getInstructions()) {
					
					if(instruction instanceof InvokeInstruction invoke &&
							pool.get(invoke.getIndex()) instanceof MethodrefConstant methodref) {
						
						var invokeDescriptor = methodref.toDescriptor();
						
						if(invokeDescriptor.getDeclaringClass() instanceof ClassType classType &&
								classType.getPackageName().startsWith("x590.jdecompiler")) {
							
							methodsUsage.put(invokeDescriptor, true);
						}
					}
				}
			}
		}
		
		
		for(var entry : methodsUsage.object2BooleanEntrySet()) {
			if(!entry.getBooleanValue()) {
				System.out.println(entry.getKey());
			}
		}
	}
}
