import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tester {
	public static class Test{
		int x;
		int y;
		
		public Test(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {	
		ArrayList<String> animals = new ArrayList<>();
		animals.add("cat3"); animals.add("dog1"); animals.add("cat2");
		animals.add("dog2"); animals.add("cat1"); animals.add("dog3");
		
		// Static way -> starts from inner methods! Needs some casting here and there - the instance way it better!
		LinqStatic.ForEach(
			(Iterable<Integer>)LinqStatic.OrderByDescending(
				LinqStatic.Select(
					LinqStatic.Where(animals, (a) -> a.startsWith("cat")),
					(a, i) -> (i + 1)),
				(a) -> (Integer) a),
			(i)->
			System.out.print(i + " "));

		System.out.println();

		// Instance way
		Linq<String> linqObject = new Linq<String>(animals);
		Iterable<Integer> result = linqObject
			.Where((a) -> a.startsWith("cat")) //Find all cats [cat3, cat2, cat1]
			.Select((a, i) -> i + 1) // Get their indexes + 1 [1, 2, 3]
			.OrderByDescending((a) -> a) // Order descending [3, 2, 1]
			.GetIterable(); // Get the array
		
		for(int s : result){
			System.out.print(s + " ");
		}
		
		/* 
		 * Output:
		 * 3 2 1
		 * 3 2 1 
		 */
		
		// GroupBy() example
		ArrayList<Test> testt = new ArrayList<>();
		testt.add(new Test(1,1));
		testt.add(new Test(1,2));
		testt.add(new Test(1,3));
		testt.add(new Test(2,1));
		testt.add(new Test(2,2));
		HashMap<Integer, ArrayList<Test>> resultt = (new Linq<Test>(testt)).GroupBy((t)->t.x);
		for(Map.Entry<Integer, ArrayList<Test>> entry : resultt	.entrySet()){
			System.out.println(entry.getKey());
			for(Test test : entry.getValue()){
				System.out.println(test.y);
			}
		}
		
	}
}
