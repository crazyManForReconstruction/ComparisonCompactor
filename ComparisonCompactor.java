import junit.framework.Assert;


@SuppressWarnings("deprecation")
public class ComparisonCompactor {
	
	private static final String ELLIPSIS = "...";
	private static final String DELTA_END = "]";
	private static final String DELTA_START = "[";
	
	private int fContextLength;
	private String fExpected;
	private String fActual;
	private int fPrefix;
	private int fSuffix;
	
	public ComparisonCompactor(int ContextLength, String Expected, String Actual){
		fContextLength = ContextLength;
		fExpected = Expected;
		fActual = Actual;
	}
	
	public String compact(String message){
		if(null==fExpected || null==fActual || areStringEqual()){
			return Assert.format(message, fExpected, fActual);
		}
		
		findCommonPrefix();
		findCommonSuffix();
		String expected = compactString(fExpected);
		String actual = compactString(fActual);
		
		/*
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
		*/
		
		return Assert.format(message, expected, actual);
		//return null;
	}
	
	private void findCommonPrefix(){
		fPrefix = 0;
		int end = Math.min(fExpected.length(), fActual.length());
		for(; fPrefix<end; fPrefix++){
			if(fExpected.charAt(fPrefix)!=fActual.charAt(fPrefix)){
				break;
			}
		}
	}
	
	private void findCommonSuffix(){
		int expectedSuffix = fExpected.length()-1;
		int actualSuffix = fActual.length()-1;
		for(; (expectedSuffix>=fPrefix)&&(actualSuffix>=fPrefix); expectedSuffix--,actualSuffix--){
			if(fExpected.charAt(expectedSuffix)!=fActual.charAt(actualSuffix)){
				break;
			}
		}
		
		fSuffix = fExpected.length()-expectedSuffix; //等同于s2.length()-sfx2
	}
	
	private String compactString(String source){
		String result = DELTA_START + source.substring(fPrefix, source.length()-fSuffix+1) + DELTA_END;
		
		if(fPrefix>0){
			result = computeCommonPrefix() + result;
		}
		
		if(fSuffix>0){
			result = result + computeCommonSuffix();
		}
		
		return result;
	}
	
	private String computeCommonPrefix(){
		return (fPrefix > fContextLength ? ELLIPSIS : "") + 
                fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
	}
	
	private String computeCommonSuffix(){
		int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
		return fExpected.substring((fExpected.length() - fSuffix + 1), end) + 
		         (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? ELLIPSIS : "");
	}
	
	private boolean areStringEqual(){
		return fExpected.equals(fActual);
	}
}
