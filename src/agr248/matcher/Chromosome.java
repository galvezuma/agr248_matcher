package agr248.matcher;

public class Chromosome {
	private String name;
	private StringBuilder str;
	
	public Chromosome(String name) {
		super();
		this.name = name;
		this.str = new StringBuilder(750_000_000);;
	}
	
	public StringBuilder getStr() {
		return str;
	}
	
	public String getName() {
		return name;
	}
}
