package latmod.coins.commands;

public enum PermLevel
{
	PLAYER(0),
	OP(4);
	
	public int level;
	
	PermLevel(int i)
	{ level = i; }
}