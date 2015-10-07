import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

public class Linq {
	public static <A, T extends Iterable<A>> Iterable<A> Where(T iterable, IPredicate<A> predicate){
		return Where(iterable, (IAction<A, Boolean>)(a)->predicate.callPredicate(a));
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> Where(T iterable, IAction<A, Boolean> action){
		ArrayList<A> result = new ArrayList<A>();
		
		for(A a : iterable){
			if(action.callAction(a)){
				result.add(a);
			};
		}
		return result;
	}
	
	public static <A, B, T extends Iterable<A>> Iterable<B> Select(T iterable, IAction<A,B> action){
		ArrayList<B> result = new ArrayList<B>();
		
		for(A a : iterable){
			result.add(action.callAction(a));
		}
		return result;
	}
	
	public static <A, B, T extends Iterable<A>> Iterable<B> Select(T iterable, IAction2Params<A, Integer, B> action){
		ArrayList<B> result = new ArrayList<B>();
		int i = 0;
		
		for(A a : iterable){
			result.add(action.callAction(a, i));
			i++;
		}
		return result;
	}
	
	public static <A, B, C extends Iterable<B>, T extends Iterable<A>> Iterable<B> 
		SelectMany(T iterable, IAction<A, C> action){
		ArrayList<B> result = new ArrayList<B>();
		
		for(A a : iterable){
			for(B b : action.callAction(a)){
				result.add(b);
			}
		}
		return result;
	}
	
	public static <A, T extends Iterable<A>> A First(T iterable){
		Iterator<A> iterator = iterable.iterator();
		
		if(iterator.hasNext()){
			return iterator.next();
		}
		return null;
	}
	
	public static <A, T extends AbstractList<A>> A Last(T abstractList){
		return abstractList.get(abstractList.size()-1);
	}
	
	public static <A, T extends Iterable<A>> A Last(T iterable){
		A result = null;
		
		for(A a : iterable){
			result = a;
		}
		return result;
	}
	
	public static <A, T extends Iterable<A>> int Count(T iterable){
		int count = 0;
		
		for(A a : iterable){
			count++;
		}
		return count;
	}
	
	public static <A, T extends Iterable<A>> int Count(T iterable, IAction<A, Boolean> action){
		return Count(Where(iterable, action));
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> Distinct(T iterable){
		ArrayList<A> result = new ArrayList<A>();
		HashSet<A> hashSet = new HashSet<A>();
		
		for(A a : iterable){
			if(!hashSet.contains(a)){
				hashSet.add(a);
				result.add(a);
			}
		}
		 
		return result;
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> OrderBy(T iterable, Comparator<A> comparator){
		ArrayList<A> result = new ArrayList<A>(Count(iterable));
		
		for(A a : iterable){
			result.add(a);
		}
		Collections.sort((List<A>) result, comparator);
		
		return result;
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> OrderByDescending(T iterable, Comparator<A> comparator){
		return OrderBy(iterable, (Comparator<A>)(t1, t2)->comparator.compare(t2, t1));
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> Reverse(T iterable){		
		LinkedList<A> list = new LinkedList<A>();
		
		for(A a : iterable){
			list.addFirst(a);
		}
		
		return list;
	}
	
	public static <A, T extends AbstractList<A>> Iterable<A> Reverse(T abstractList){
		    return new Iterable<A>() {

		        @Override
		        public Iterator<A> iterator() {
		            return new Iterator<A>() {

		            	int i = abstractList.size();
		            	
		                @Override
		                public boolean hasNext() {
		                    return i > 0;
		                }

		                @Override
		                public A next() {
		                    return abstractList.get(--i);
		                }

		                @Override
		                public void remove() {
		                	if(i < 0 || i >= abstractList.size()){
		                		return;
		                	}
		                	abstractList.remove(i);
		                }

		            };
		        }
		    };
	}
	
	public static <A, T extends Iterable<A>> void ForEach(T iterable, IAction<A, Void> action){
		for(A a : iterable){
			action.callAction(a);
		}
	}

	public static <A, T extends Iterable<A>> Iterable<A> Take(T iterable, int toTakeCount){
		return new Iterable<A>() {

	        @Override
	        public Iterator<A> iterator() {
	            return new Iterator<A>() {

	            	Iterator<A> iterator = iterable.iterator();
	            	int i = -1;
	            	
	                @Override
	                public boolean hasNext() {
	                    return iterator.hasNext() && i < toTakeCount-1;
	                }

	                @Override
	                public A next() {
	                	i++;
	                    return iterator.next();
	                }
	            };
	        }
	    };
	}
	
	public static <A, T extends Iterable<A>> Iterable<A> Skip(T iterable, int toSkipCount){
		Iterator<A> iterator = iterable.iterator();
		for(int i = 0; i < toSkipCount; i++){
			if(iterator.hasNext()){
				iterator.next();
			}
		}
		if(!iterator.hasNext()){
			return new ArrayList<A>();
		}
		
		return new Iterable<A>() {

	        @Override
	        public Iterator<A> iterator() {
	            return new Iterator<A>() {
	            	
	                @Override
	                public boolean hasNext() {
	                    return iterator.hasNext();
	                }

	                @Override
	                public A next() {
	                    return iterator.next();
	                }
	            };
	        }
	    };
	}
}
