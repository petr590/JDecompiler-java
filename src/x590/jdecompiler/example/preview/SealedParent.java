package x590.jdecompiler.example.preview;

import java.io.Serializable;

public sealed abstract class SealedParent implements Serializable permits SealedParent.G.InnerChild1, SealedParent.InnerChild2, SealedChild1, SealedChild2 {
	
	public static class G {
		
		public int sub(int non, int sealed, int permits) {
			return non-sealed-permits;
		}
		
		public non-sealed class InnerChild1 extends SealedParent {}
	}
	
	public sealed class InnerChild2<T> extends SealedParent {}
	
	public class H {
		
		public final class InnerChild3 extends InnerChild2<String> {}
		
		public final class InnerChild4 extends InnerChild2<Integer> {}
	}
}
