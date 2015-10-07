import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

//import Select;

public class Tester {

	public static class Point{
		int x;
		int y;
		
		public Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	public static class Test{
		ArrayList<String> test;
		
		public Test(ArrayList<String> test){
			this.test = test;
		}
	}
	
	public static void main(String[] args) {
		ArrayList<Point> b = new ArrayList<Point>();
		b.add(new Point(1, 1));
		b.add(new Point(1, 2));
		b.add(new Point(1, 3));
		b.add(new Point(1, 4));
		b.add(new Point(1, 5));
		b.add(new Point(1, 6));
		b.add(new Point(1, 7));
		b.add(new Point(1, 8));
		b.add(new Point(1, 9));
		b.add(new Point(1, 10));
		for(int y : Linq.Select(b, (IAction<Point, Integer>)(t) -> t.y)){
			System.out.print(y + " ");
		}
		System.out.println();
		
		for(Point point : Linq.Where(b, (IPredicate<Point>)(t) -> t.y>5)){
			System.out.print(point.y + " ");
		}
		System.out.println();
		
		for(String string : Linq.Select(b, (IAction2Params<Point, Integer, String>)(t, i)->String.format("%s: %s", i, t.y))){
			System.out.print(string + " ");
		}
		System.out.println();
		
		ArrayList<String> test1 = new ArrayList<String>();
		ArrayList<String> test2 = new ArrayList<String>();
		ArrayList<Test> test = new ArrayList<Test>();
		test1.add("01");test1.add("02");test1.add("03");
		test2.add("06");test2.add("05");test2.add("04");
		test2.add("06");test2.add("05");test2.add("04");
		test.add(new Test(test1));test.add(new Test(test2));
		
		for(String string : Linq.SelectMany(test, (IAction<Test, ArrayList<String>>)(t)->t.test)){
			System.out.print(string + " ");
		}
		
		ListIterator li = test2.listIterator();
		System.out.println(test2);
		System.out.println(Linq.Distinct(test2));
		System.out.println(Linq.OrderByDescending(test2, (Comparator<String>)((t1, t2)->t2.compareTo(t1))));
		Linq.ForEach(Linq.Skip(test2, 2), (IAction<String, Void>)(t)->{
			System.out.println(t);		
			return (null);
		});
	}
	
}
