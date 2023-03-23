package x590.jdecompiler.testing;

import java.util.HashSet;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
		packagesOf = UnusedTesting.class,
		importOptions = {
				ImportOption.DoNotIncludeArchives.class,
				ImportOption.DoNotIncludeJars.class,
				ImportOption.DoNotIncludeTests.class
		}
)
public class UnusedTesting {
	@ArchTest
	public static ArchRule methodsShouldNotBeUnused = methods()
			.that().doNotHaveName("equals")
			.and().doNotHaveName("hashCode")
			.and().doNotHaveName("toString")
			.and().doNotHaveName("main")
			/*.and(not(methodHasAnnotationThatEndsWith("Handler")
					.or(methodHasAnnotationThatEndsWith("Listener"))
					.or(methodHasAnnotationThatEndsWith("Scheduled"))
					.and(declaredIn(describe("component", clazz -> clazz.isMetaAnnotatedWith(Component.class))))
			))*/.should(new ArchCondition<>("not be unreferenced") {
				@Override
				public void check(JavaMethod javaMethod, ConditionEvents events) {
					var accesses = new HashSet<>(javaMethod.getAccessesToSelf());
					accesses.removeAll(javaMethod.getAccessesFromSelf());
					if(accesses.isEmpty()) {
						events.add(new SimpleConditionEvent(javaMethod, false,
								String.format("%s is unreferenced in %s", javaMethod.getDescription(), javaMethod.getSourceCodeLocation())));
					}
				}
			});
	
//	static DescribedPredicate<JavaMethod> methodHasAnnotationThatEndsWith(String suffix) {
//		return describe(String.format("has annotation that ends with '%s'", suffix),
//				method -> method.getAnnotations().stream()
//				.anyMatch(annotation -> annotation.getRawType().getFullName().endsWith(suffix)));
//	}
}
