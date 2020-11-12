
public class Polynomial{
	
	public Polynomial(int a){
		this.polynomial=a;
	}
	private int polynomial;

	public int get(){
		return this.polynomial;
	}

	public void set(int a){
		this.polynomial=a;
	}
	
	private static final int mod=283;
	
	public static Polynomial add(Polynomial p1,Polynomial p2){
		int a=p1.get();
		int b=p2.get();
		int c=Polynomial.modulus(a^b);
		return new Polynomial(c);
	}

	public static Polynomial mul(Polynomial p1,Polynomial p2){
		int product=0;

		int multiplier=p2.get();
		int multiplicand=p1.get();

		while(multiplier>0){
			if(multiplier%2==1){
				product=Polynomial.modulus(product^multiplicand);
			}

			multiplier=multiplier>>1;
			multiplicand=multiplicand<<1;
			multiplicand=Polynomial.modulus(multiplicand);
		}

		return new Polynomial(product);
	}

	private static int log2(int a){

		int b=0;
		while(a>0){
			a=a>>1;
			b=b+1;
		}

		return b;
	}

	public static Polynomial quotient(Polynomial p1,Polynomial p2){

		int a=p1.get();
		int b=p2.get();

		int q=0;
		int t1=Polynomial.log2(a);
		int t2=Polynomial.log2(b);

		while(t1>=t2){
			q=q|(1<<(t1-t2));
			a=a^(b<<(t1-t2));
			t1=Polynomial.log2(a);
			t2=Polynomial.log2(b);
		}

		return new Polynomial(q);
	}

	public static Polynomial inverse(Polynomial p1){

		Polynomial a1=new Polynomial(1);
		Polynomial a2=new Polynomial(0);
		Polynomial a3=new Polynomial(Polynomial.mod);

		Polynomial b1=new Polynomial(0);
		Polynomial b2=new Polynomial(1);
		Polynomial b3=new Polynomial(p1.get());

		while(b3.polynomial!=1){

			Polynomial quo=Polynomial.quotient(a3,b3);

			Polynomial t1=b1;
			Polynomial t2=b2;
			Polynomial t3=b3;

			b1=Polynomial.add(a1,Polynomial.mul(quo,b1));
			b2=Polynomial.add(a2,Polynomial.mul(quo,b2));
			
			b3=Polynomial.add(a3,Polynomial.mul(quo,b3));
			
			a1=t1;
			a2=t2;
			a3=t3;

		}
		return b2;
	}

	public static int hexVal(char c){

		int charval=Character.getNumericValue(c);
		int zeroval=Character.getNumericValue('0');
		int a=Character.getNumericValue('a');

		if(charval-zeroval>=0 && charval-zeroval<=9)
			return charval-zeroval;

		return charval-a+10;
	}
    
    public static int hexVal1(char c,char d){

		int charval=Character.getNumericValue(c);
        int charval1=Character.getNumericValue(d);
		int zeroval=Character.getNumericValue('0');
		int a=Character.getNumericValue('a');
        int l=0,m=0;
		if(charval-zeroval>=0 && charval-zeroval<=9){
			m=charval-zeroval;
    }else{m=charval-a+10;}
        if(charval1-zeroval>=0 && charval1-zeroval<=9){
			l=charval1-zeroval;
    }else{
        l=charval1-a+10;}
        return l*16+m;
	}

	private static int modulus(int m){
		if(m<256)
			return m;

		m=m^Polynomial.mod;
		return m;
	}

}
