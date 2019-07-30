// STUDENT NAME: Yinsheng Dong
// STUDENT NUMBER: 11148648
// NSID: yid164
// LECTURE SECTION: CMPT 280

import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import sun.jvm.hotspot.HelloWorld;

import java.util.Random;
import java.util.LinkedList.*;

// the function given
public class A1Q1 {

    public static Sack[] generatePlunder(int howMany){
        Random generator = new Random();
        Sack grain[] = new Sack[howMany];

        for (int i = 0; i < howMany; i++){
            grain[i] = new Sack(
                    Grain.values()[generator.nextInt(Grain.values().length)],
                    generator.nextDouble() * 100 );

        }

        return grain;
    }

@SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        // create a linked list sack, size is the grain's values length, should be 5
        LinkedList280<Sack> sacks[] = new LinkedList280[Grain.values().length];

        // create an array list of Sack, save the generatePlunder, run it for get numbers
        Sack gp [] = generatePlunder(Grain.values().length);

        // use the for-loops to generate linkedList in different array
        for (int i=0; i< sacks.length; i++)
        {
            sacks[i] = new LinkedList280<Sack>();

        }

        for (int i=0; i<sacks.length; i++)
        {
            Sack check;

            check = gp[i];
            // This is the Barley type
            if(check.getType().equals(Grain.BARLEY))
            {
                sacks[Grain.BARLEY.ordinal()].insert(check);

            }
            // This is the Wheat type
            else if (check.getType().equals(Grain.WHEAT))
            {
                sacks[Grain.WHEAT.ordinal()].insert(check);

            }
            // This is the Rye type
            else if(check.getType().equals(Grain.RYE))
            {
                sacks[Grain.RYE.ordinal()].insert(check);
            }
            // This is the Oats type
            else if(check.getType().equals(Grain.OATS))
            {
                sacks[Grain.OATS.ordinal()].insert(check);
            }
            // this is the Other type
            else if(check.getType().equals(Grain.OTHER))
            {
                sacks[Grain.OTHER.ordinal()].insert(check);
            }
            else
            {
                return;
            }



        }

        // make 5 iterator to go through the 5 linkedlists, to calculate the total wight of one of them
        LinkedIterator280<Sack> wheatIterator = new LinkedIterator280<Sack>(sacks[Grain.WHEAT.ordinal()]);
        LinkedIterator280<Sack> barleyIterator = new LinkedIterator280<Sack>(sacks[Grain.BARLEY.ordinal()]);
        LinkedIterator280<Sack> oatsIterator = new LinkedIterator280<Sack>(sacks[Grain.OATS.ordinal()]);
        LinkedIterator280<Sack> ryeIterator = new LinkedIterator280<Sack>(sacks[Grain.RYE.ordinal()]);
        LinkedIterator280<Sack> otherIterator = new LinkedIterator280<Sack>(sacks[Grain.OTHER.ordinal()]);
        // to calculate total of wheat
        double wheatWight=0;
        while(wheatIterator.itemExists())
        {
            wheatWight += wheatIterator.item().getWeight();
            wheatIterator.goForth();
        }
        // to calculate total of barley
        double barleyWight=0;
        while(barleyIterator.itemExists())
        {
            barleyWight += barleyIterator.item().getWeight();
            barleyIterator.goForth();
        }
        // to calculate total of oats
        double oatsWight=0;
        while(oatsIterator.itemExists())
        {
            oatsWight+=oatsIterator.item().getWeight();
            oatsIterator.goForth();
        }
        // to calculate total of rye
        double ryeWight=0;
        while (ryeIterator.itemExists())
        {
            ryeWight+=ryeIterator.item().getWeight();
            ryeIterator.goForth();
        }
        // to calculate total of other
        double otherWight=0;
        while(otherIterator.itemExists())
        {
            otherWight+=otherIterator.item().getWeight();
            otherIterator.goForth();
        }



        // Jack named to system out of total of them.
        System.out.println("Jack plundered "+wheatWight+" pounds of WHEAT ");
        System.out.println("Jack plundered "+barleyWight+" pounds of BARLEY ");
        System.out.println("Jack plundered "+oatsWight+" pounds of OATS ");
        System.out.println("Jack plundered "+ryeWight+" pounds of RYE ");
        System.out.println("Jack plundered "+otherWight+" pounds of OTHER ");




    }
}
