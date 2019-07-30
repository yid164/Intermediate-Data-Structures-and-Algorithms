// Name: Yinsheng Dong
// Student Number: 11148648
// NSID: yid164
// Lecture: CMPT 280


import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;
import lib280.tree.MAryIterator280;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1)
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}


    /**
     * finding function is a helper function for skillDependencies and skillTotalCost function
     * @param Skill skill and LinkedList280<Skill> skills
     * @return true if root item equals the skill, false otherwise
     */
	private boolean finding (Skill skill, LinkedList280<Skill> skills)
    {
        // if the root item equals the skill, true and insert the root item into the linkedList skills
        if(this.rootItem().equals(skill))
        {
            skills.insert(this.rootItem());
            return true;
        }
        // if there are no non empty child, false
        else if (this.rootLastNonEmptyChild()==0)
        {
            return false;
        }
        // otherwise, use  recursive traversal to find the skill, return false if the skill does not exsits
        else
        {
            boolean b = false;
            for(int i=1; i<= this.rootLastNonEmptyChild(); i++)
            {
                b = b||rootSubTree(i).finding(skill,skills);
            }
            if(b)
            {
                skills.insert(this.rootItem());
            }
            return b;
        }
    }



    /**
     * skillDependencies function, a linkedlist to store the skill's pre-require
     * @param skill, the pre-require skills that want to see
     * @return linked list that store the skill's pre-require skill
     */
	public LinkedList280<Skill> skillDependencies(Skill skill) throws RuntimeException
	{
        // new linkedlist to store skills
        LinkedList280<Skill> skills = new LinkedList280<Skill>();
        // if the helper function is return false, the skill is not found
        if(!finding(skill,skills))
        {
            throw new RuntimeException("The skill is not found");
        }
        // otherwise, return the skills list
        return skills;
	}

    /**
     * skillTotalCost function
     * @param skill the total cost of the skills
     * @
     */
	public int skillTotalCost(Skill skill) throws RuntimeException
    {
        // create a new linkedlist skills
        LinkedList280<Skill> skills = new LinkedList280<Skill>();
        // if the helper function is return false, the skill is not found
        if(!finding(skill,skills))
        {
            throw new RuntimeException("The skill is not found");
        }
        // move the cusor to the first skill
        skills.goFirst();
        // set up a total number
        int total = 0;
        // while the list is not in the end, add them together
        while (!skills.after())
        {
            total = total+skills.item().skillCost;
            skills.goForth();
        }
        return total;
    }

    /**
     * The test function for the skill tree
     */
	public static void main(String [] args)
	{
        
        // 10 skills
		Skill freeze = new Skill("Freeze","Freezes troops and buildings", 3);
		Skill iceTower = new Skill("Ice Tower", "Build a tower that has ability to freeze one enemy",3);
		Skill snowBall = new Skill("Snow Ball", "A big snow ball push enemy out", 2);
		Skill iceBall = new Skill ("Ice Ball", "A small ice ball to hit enemy", 1);
		Skill snowFlake = new Skill("Snowflake", "Some snowflake to disturb enemy",3);
		Skill iceTear = new Skill("Ice Tear", "A drop of ice tear can regain any of one's own side army", 3);
		Skill iceArrow = new Skill("Ice Arrow", "10 ice arrows hit a large area", 2);
		Skill snowMan = new Skill("Snow Man", "Summon a snow man, and it owns the skill 'FREEZE'",5);
		Skill iceStorm = new Skill ("Ice Storm", "A Spell that can destory any building", 7);
		Skill iceWorld = new Skill("Ice World", "Change the world to a ICE WORLD, all ice skills gain 20% effective", 7);
        
        // the fakeSkill for test the exception
		Skill fakeSkill = new Skill("Fake Skill", "For testing the non-added skills",1);
        // ice those 10 skills into a list
		Skill[] iceSkills = new Skill[10];
		iceSkills[0] = freeze;
		iceSkills[1] = iceTear;
		iceSkills[2] = snowBall;
		iceSkills[3] = iceBall;
		iceSkills[4] = snowFlake;
		iceSkills[5] = iceArrow;
		iceSkills[6] = iceTower;
		iceSkills[7] = snowMan;
		iceSkills[8] = iceWorld;
		iceSkills[9] = iceStorm;

        // the first tree has 2 children
		SkillTree first = new SkillTree(iceSkills[0], 2);
        // the second 2 trees both have 2 children
		SkillTree second0 = new SkillTree(iceSkills[1],2);
		SkillTree second1 = new SkillTree(iceSkills[2],2);
		first.setRootSubtree(second0,1);
		first.setRootSubtree(second1,2);
        // the third 1st tree has 0
        // the left of third trees both have 1 child
		SkillTree thrid0 = new SkillTree(iceSkills[3],0);
		SkillTree thrid1 = new SkillTree(iceSkills[4],1);
		SkillTree thrid2 = new SkillTree(iceSkills[5],1);
		SkillTree thrid3 = new SkillTree(iceSkills[6],1);
		second0.setRootSubtree(thrid0,1);
		second0.setRootSubtree(thrid1,2);
		second1.setRootSubtree(thrid2,1);
		second1.setRootSubtree(thrid3,2);
        // the fourth 3 tree have no child
		SkillTree fourth0 = new SkillTree(iceSkills[7],0);
		SkillTree fourth1 = new SkillTree(iceSkills[8],0);
		SkillTree fourth2 = new SkillTree(iceSkills[9],0);
		thrid1.setRootSubtree(fourth0,1);
		thrid2.setRootSubtree(fourth1,1);
		thrid3.setRootSubtree(fourth2,1);

		System.out.println("My Skill Tree: ");
		System.out.println(first.toStringByLevel());


		System.out.println("Dependencies for Ice Ball: " );
		System.out.println(first.skillDependencies(iceBall));
		System.out.println("Dependencies for Snow Man: ");
		System.out.println(first.skillDependencies(snowMan));
		System.out.println("Dependencies for Freeze: ");
		System.out.println(first.skillDependencies(freeze));
		System.out.println("Dependencies for FakeSkill");
        
        // test the skillDependencies exception by using the fakeSkill
		try
        {
            first.skillDependencies(fakeSkill);
        }
        catch (RuntimeException e)
        {
            System.out.println("Fake skill is not found");
        }

		System.out.println("To get Snow Ball you must invest " + first.skillTotalCost(snowBall) +" points");
        System.out.println("To get Ice Storm you must invest " + first.skillTotalCost(iceStorm) +" points");
        System.out.println("To get Ice Tear you must invest " + first.skillTotalCost(iceTear) +" points");
        
        // test the skillTotalCost exception by using the fakeSkill
        try
        {
            first.skillTotalCost(fakeSkill);
        }
        catch (RuntimeException e)
        {
            System.out.println("Fake skill is not found");
        }

	}
	

}
