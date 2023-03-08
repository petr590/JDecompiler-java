package x590.jdecompiler.scope;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import x590.jdecompiler.Version;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.IntegerConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.PrimitiveType;
import x590.util.BooleanHolder;
import x590.util.Util;

public class SwitchScope extends Scope {
	
	private final int defaultIndex;
	private final Int2ObjectMap<List<IntegerConstant>> indexTable;
	private final Operation value;
	
	private void addIndexToTable(ConstantPool pool, int index, IntegerConstant value) {
		
		List<IntegerConstant> list = indexTable.get(index);
		
		if(list == null) {
			indexTable.put(index, list = new ArrayList<>());
		}
		
		list.add(value);
	}
	
	public SwitchScope(DecompilationContext context, int defaultIndex, Int2IntMap offsetTable) {
		super(context, defaultIndex);
		
		var pool = context.pool;
		
		this.defaultIndex = defaultIndex;
		
		this.indexTable = new Int2ObjectOpenHashMap<>(offsetTable.size());
		
		for(var entry : offsetTable.int2IntEntrySet()) {
			addIndexToTable(pool, context.posToIndex(entry.getIntValue() + context.currentPos()), pool.findOrCreateConstant(entry.getIntKey()));
		}
		
		
		this.value = context.popAsNarrowest(PrimitiveType.INT);
		
		this.indexes = new IntArrayList(indexTable.keySet());
		indexes.sort((x, y) -> y - x);
		
		setEndIndex(indexes.isEmpty() ? defaultIndex : Math.max(defaultIndex, indexes.getInt(0)));
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("switch(").print(value, context).write(')');
	}
	
	
	private IntList indexes;
	
	@Override
	protected void writeBody(StringifyOutputStream out, StringifyContext context) {
		
		var indexTable = this.indexTable;
		var indexes = this.indexes;
		boolean canUseNewSwitch = context.getClassinfo().getVersion().majorVersion >= Version.JAVA_12 &&
				!indexTable.containsKey(defaultIndex);
		
		var defaultCaseWrote = new BooleanHolder();
		
		var type = this.value.getReturnType();
		
		out.increaseIndent(2);
		
		Util.forEachExcludingLast(getCode(),
				
				(operation, index) -> {
					index = getIndexByCodeIndex(index);
					
					if(canUseNewSwitch && !defaultCaseWrote.get() && index >= defaultIndex) {
						out.reduceIndent().println().printIndent().print("default -> ").increaseIndent();
						defaultCaseWrote.set(true);
						
					} else {
						int lastIndexIndex = indexes.size() - 1;
						
						if(lastIndexIndex > 0) {
							int lastIndex = indexes.getInt(lastIndexIndex);
							
							if(index >= lastIndex) {
								indexes.removeInt(lastIndexIndex);
								
								List<IntegerConstant> constants = indexTable.get(lastIndex);
								
								out.reduceIndent().println().printIndent();
								
								if(canUseNewSwitch) {
									out.write("case ");
									
									Util.forEachExcludingLast(constants,
											constant -> constant.writeTo(out, context.getClassinfo(), type),
											constant -> out.write(", ")
									);
									
									out.write(" -> ");
									
								} else {
									Util.forEachExcludingLast(constants,
											constant -> {
												out.write("case ");
												constant.writeTo(out, context.getClassinfo(), type);
												out.write(':');
											},
											
											constant -> out.writesp()
									);
									
									if(defaultCaseWrote.isFalse() && index >= defaultIndex) {
										if(!constants.isEmpty())
											out.writesp();
										out.write("default:");
										
										defaultCaseWrote.set(true);
									}
								}
								
								out.increaseIndent();
								
							}
						}
					}
					
					operation.writeAsStatement(out, context);
				},
				
				operation -> operation.writeSeparator(out, context)
			);
		
		out.reduceIndent(2);
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
}
