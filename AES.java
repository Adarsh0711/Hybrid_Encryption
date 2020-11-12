
public class AES{

	public AES(String text,String key){
		this.key=new Keys(key);
        this.Sbox=new Sbox1(key);
		this.text=new Polynomial[4][4];
		this.setText(text);
		
	}

	private void setText(String text){

		int firstHalfByte;
		int secondHalfByte;

		for(int i=0;i<32;i+=8){
			for(int j=0;j<8;j+=2){
				firstHalfByte=Polynomial.hexVal(text.charAt(i+j));
				secondHalfByte=Polynomial.hexVal(text.charAt(i+j+1));

				this.text[(i>>3)][(j>>1)]=new Polynomial((firstHalfByte<<4|secondHalfByte));
			}
		}
	}

	private void addRoundKey(int round){

		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.add(this.text[i][j],key.getKey(round*4+i,j));
			}
		}
	}

	private void subBytes(){
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				
				int row=(this.text[i][j].get()&(15<<4))>>4;
				int col=this.text[i][j].get()&15;

				this.text[i][j].set(Sbox.getSbox(row,col));
			}
	}
    
	private void invSubBytes(){
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				
				int row=(this.text[i][j].get()&(15<<4))>>4;
				int col=this.text[i][j].get()&15;

				this.text[i][j].set(Sbox.getInvSbox(row,col));
			}
	}

	private void shiftRows(){
		
		Polynomial temp[]=new Polynomial[4];
		for(int i=0;i<4;i++){

			for(int j=0;j<4;j++)
				temp[j]=this.text[j][i];

			for(int j=0;j<4;j++){
				this.text[j][i]=temp[(j+i)%4];
			}
		}
	}

	private void invShiftRows(){
		
		Polynomial temp[]=new Polynomial[4];
		for(int i=0;i<4;i++){

			for(int j=0;j<4;j++)
				temp[j]=this.text[j][i];

			for(int j=0;j<4;j++){
				this.text[j][i]=temp[(j-i+4)%4];
			}
		}
	}

	private void mixColumns(){

		Polynomial temp[]=new Polynomial[4];

		for(int i=0;i<4;i++){
			
			for(int j=0;j<4;j++){
				temp[j]=this.text[i][j];
			}

			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.mul(temp[j], new Polynomial(2));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(3),temp[(j+1)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],temp[(j+2)%4]);
				this.text[i][j]=Polynomial.add(this.text[i][j],temp[(j+3)%4]);
			}
		}
	}

	private void invMixColumns(){

		Polynomial temp[]=new Polynomial[4];

		for(int i=0;i<4;i++){
			
			for(int j=0;j<4;j++){
				temp[j]=this.text[i][j];
			}

			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.mul(temp[j], new Polynomial(0x0E));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x0B),temp[(j+1)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x0D),temp[(j+2)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x09),temp[(j+3)%4]));
			}
		}
	}

	public String encrypt(){

		for(int i=0;i<9;i++){
			
			this.addRoundKey(i);
			this.subBytes();
			this.shiftRows();
			this.mixColumns();
		}

		this.addRoundKey(9);
		this.subBytes();
		this.shiftRows();
		this.addRoundKey(10);

		return this.toString();
		
		
	}

	public String decrypt(){

		
		this.addRoundKey(10);
		this.invShiftRows();
		this.invSubBytes();
		
		for(int i=9;i>0;i--){
			
			this.addRoundKey(i);
			this.invMixColumns();
			this.invShiftRows();
			this.invSubBytes();
		}

		this.addRoundKey(0);

		return this.toString();
	}

	public String toString(){

		String s="";
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				s+=String.format("%02X", this.text[i][j].get());
			}
		}
		return s;
	}

	public void print(){
		System.out.println(this.toString());
	}

	private Polynomial text[][];

	private Keys key;

    private Sbox1 Sbox;
}
