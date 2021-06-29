public class Character 
{
    private final String m_name;
    private int m_HP;
    private int m_damage_per_move;    
    private int m_gold;

    public Character(String name, int HP, int damage_per_move, int gold)
    {
        m_name = name;
        m_HP = HP;
        m_damage_per_move = damage_per_move;
        m_gold = gold;
    }

    // getter functions for everything
    // setter functions for non-final instance variables

    // toString() function to print Character status
    public String toString()
    {
        String s = "Status:\n";
        s += "Name           : " + m_name + "\n";
        s += "HP             : " + m_HP + "\n";
        s += "Damage Per Move: " + m_damage_per_move + "\n";
        s += "Gold           : " + m_gold + "\n";
        return s;
    }
}
