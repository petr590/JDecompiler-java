package x590.jdecompiler.scope;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.CodeAttribute;
import x590.jdecompiler.attribute.LocalVariableTableAttribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.array.NewArrayOperation;
import x590.jdecompiler.operation.load.ILoadOperation;
import x590.jdecompiler.operation.returning.AReturnOperation;
import x590.jdecompiler.operation.returning.ReturnOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.variable.NamedVariable;
import x590.jdecompiler.variable.UnnamedVariable;
import x590.jdecompiler.variable.Variable;
import x590.jdecompiler.variable.VariableWrapper;
import x590.util.annotation.Nullable;
import x590.util.function.ObjIntFunction;
import x590.util.lazyloading.ObjectSupplierLazyLoading;

public class MethodScope extends Scope {
	
	private static final ObjectSupplierLazyLoading<MethodScope> EMPTY_SCOPE = new ObjectSupplierLazyLoading<>(MethodScope::new);
	private final ObjectSupplierLazyLoading<NewArrayOperation> lambdaNewArray = new ObjectSupplierLazyLoading<>(this::getLambdaNewArray);
	
	private MethodScope() {
		super(0, 0, null, Collections.emptyList());
	}
	
	private MethodScope(ClassInfo classinfo, MethodDescriptor genericDescriptor, MethodModifiers modifiers, CodeAttribute codeAttribute, int endIndex, int maxLocals) {
		super(0, endIndex, null, new ArrayList<>(maxLocals));
		
		int i = 0;
		
		if(modifiers.isNotStatic()) {
			addVariable(new NamedVariable("this", this, classinfo.getThisType(), true).defined());
			i++;
		}
		
		
		LocalVariableTableAttribute localVariableTable = codeAttribute.getAttributes().getNullable(AttributeType.LOCAL_VARIABLE_TABLE);
		
		
		final var emptyVar = VariableWrapper.empty();
		
		// public static void main(String[] args)
		if(localVariableTable == null &&
				modifiers.allOf(ACC_PUBLIC | ACC_STATIC) &&
				genericDescriptor.equalsIgnoreClass(PrimitiveType.VOID, "main", ArrayType.STRING_ARRAY)) {
			
			addVariable(new NamedVariable("args", this, ArrayType.STRING_ARRAY, true).defined());
			
		} else {
			
			int codeLength = codeAttribute.getCode().length;
			
			ObjIntFunction<Type, Variable> variableCreator = localVariableTable != null ?
					(argType, index) -> Variable.valueOf(localVariableTable.findEntry(index, codeLength), this, argType, true) :
					(argType, index) -> new UnnamedVariable(this, argType, true);
			
			
			for(Type argType : genericDescriptor.getArguments()) {
				addVariable(variableCreator.apply(argType, i).defined());
				
				if(argType.getSize() == TypeSize.LONG) {
					addVariable(emptyVar);
					i++;
				}
				
				i++;
			}
		}
		
		
		for(; i < maxLocals; i++)
			addVariable(emptyVar);
	}
	
	
	public static MethodScope of(ClassInfo classinfo, MethodDescriptor genericDescriptor, MethodModifiers modifiers, CodeAttribute codeAttribute, int endIndex, int maxLocals) {
		return endIndex == 0 && maxLocals == 0 ? EMPTY_SCOPE.get() : new MethodScope(classinfo, genericDescriptor, modifiers, codeAttribute, endIndex, maxLocals);
	}
	
	/** Меняет область видимости на public */
	@Override
	public void reduceVariablesTypes() {
		super.reduceVariablesTypes();
	}
	
	/** Меняет область видимости на public */
	@Override
	public void defineVariables() {
		super.defineVariables();
	}
	
	/** Меняет область видимости на public */
	@Override
	public void assignVariablesNames() {
		super.assignVariablesNames();
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
	
	
	@Override
	public void initLabel() {
		throw new UnsupportedOperationException("Cannot write label from MethodScope");
	}
	
	
	private @Nullable NewArrayOperation getLambdaNewArray() {
		List<Operation> operations = getOperations();
		
		if(operations.size() == 1 &&
				operations.get(0) instanceof AReturnOperation areturn &&
				areturn.getOperand() instanceof NewArrayOperation newArray) {
			
			List<Operation> lengths = newArray.getLengths();
			
			if(lengths.size() == 1 && lengths.get(0) instanceof ILoadOperation iload &&
					iload.getVariable() == getDefinedVariable(0)) {
				
				return newArray;
			}
		}
		
		return null;
	}
	
	
	public void writeAsLabmda(StringifyOutputStream out, StringifyContext context) {
		List<Operation> operations = getOperations();
		
		if(operations.isEmpty()) {
			out.write(" {}");
			
		} else if(operations.size() == 1) {
			Operation first = operations.get(0);
			
			if(first.canInlineInLambda()) {
				out.printsp().print(first instanceof ReturnOperation returnOperation ? returnOperation.getOperand() : first, context);
				return;
			}
		}
		
		this.writeTo(out, context);
	}
	
	
	public boolean isLambdaNewArray() {
		return lambdaNewArray.get() != null;
	}
	
	
	public boolean tryWriteAsLambdaNewArray(StringifyOutputStream out, StringifyContext context) {
		var newArrayOperation = lambdaNewArray.get();
		
		if(newArrayOperation != null) {
			out.print(newArrayOperation.getReturnType(), context.getClassinfo()).print("::new");
			return true;
		}
		
		return false;
	}
}
