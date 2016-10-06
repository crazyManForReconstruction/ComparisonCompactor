import junit.framework.Assert;


@SuppressWarnings("deprecation")
public class ComparisonCompactor {
	private int ctxt;
	private String s1;
	private String s2;
	private int pfx;
	private int sfx;
	
	public ComparisonCompactor(int ctxt, String s1, String s2){
		this.ctxt = ctxt;
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String compact(String msg){
		if(null==s1 || null==s2 || s1.equals(s2)){
			return Assert.format(msg, s1, s2);
		}
		
		pfx = 0;
		for(; pfx<Math.min(s1.length(), s2.length()); pfx++){
			if(s1.charAt(pfx)!=s2.charAt(pfx)){
				break;
			}
		}
		
		int sfx1 = s1.length()-1;
		int sfx2 = s2.length()-1;
		for(; (sfx1>=pfx)&&(sfx2>=pfx); sfx1--,sfx2--){
			if(s1.charAt(sfx1)!=s2.charAt(sfx2)){
				break;
			}
		}
		
		sfx = s1.length()-sfx1; //等同于s2.length()-sfx2
		
		System.out.print("pfx: ");
		System.out.println(pfx);
		System.out.print("s1.length()-sfx1: ");
		System.out.println(s1.length()-sfx1);
		System.out.print("s2.length()-sfx2: ");
		System.out.println(s2.length()-sfx2);
		
		String cmp1 = compactString(s1);
		System.out.println(cmp1);
		String cmp2 = compactString(s2);
		System.out.println(cmp2);
		
		return Assert.format(msg, s1, s2);
		//return null;
	}
	
	private String compactString(String s){
		String result = "[" + s.substring(pfx, s.length()-sfx+1) + "]";
		
		if(pfx>0){
			result = (pfx > ctxt ? "..." : "") + s1.substring(Math.max(0, pfx - ctxt), pfx) + result;
		}
		
		if(sfx>0){
			int end = Math.min(s1.length() - sfx + 1 + ctxt, s1.length());
			result = result + s1.substring((s1.length() - sfx + 1), end) + 
			         (s1.length() - sfx + 1 < s1.length() - ctxt ? "..." : "");
		}
		return result;
	}
}
