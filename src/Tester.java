import java.util.ArrayList;

public class Tester {
	public static class Test{
		int x;
		
		public Test(int x){
			this.x = x;
		}
	}
	
	public static void main(String[] args) {	
		ArrayList<String> animals = new ArrayList<String>();
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
	}
}
