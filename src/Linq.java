import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

public class Linq<TInitial> {
	public interface IFunc <TParam, TResult> {
		public TResult callAction(TParam parameter);
	}
	
	public interface IFunc2Params<TParam1, TParam2, TResult> {
		public TResult callAction(TParam1 parameter1, TParam2 parameter2);
	}
	
	public interface IAction<TParam1> {
		public void callAction(TParam1 parameter1);
	}
	
	private boolean extendsAbstractList = false;
	private Iterable<TInitial> _iterable;
	
	public <TIterable extends Iterable<TInitial>> Linq(TIterable iterable){
		_iterable = iterable;
	}
	
	public <TIterable extends AbstractList<TInitial>> Linq(TIterable iterable){
		_iterable = iterable;
		extendsAbstractList = true;
	}
	
	public Iterable<TInitial> GetIterable(){
		return _iterable;
	}
	
	public Linq<TInitial> Where(IFunc<TInitial, Boolean> action){
		ArrayList<TInitial> result = new ArrayList<TInitial>();
		
		for(TInitial a : _iterable){
			if(action.callAction(a)){
				result.add(a);
			};
		}
		
		return new Linq<TInitial>(result);
	}
	
	public <B> Linq<B> Select(IFunc<TInitial,B> action){
		ArrayList<B> result = new ArrayList<B>();
		
		for(TInitial a : _iterable){
			result.add(action.callAction(a));
		}
		return new Linq<B>(result);
	}
	
	public <B> Linq<B> Select(IFunc2Params<TInitial, Integer, B> action){
		ArrayList<B> result = new ArrayList<B>();
		int i = 0;
		
		for(TInitial a : _iterable){
			result.add(action.callAction(a, i));
			i++;
		}
		return new Linq<B>(result);
	}
	
	public <B, C extends Iterable<B>> Linq<B> SelectMany(IFunc<TInitial, C> action){
		ArrayList<B> result = new ArrayList<B>();
		
		for(TInitial a : _iterable){
			for(B b : action.callAction(a)){
				result.add(b);
			}
		}
		
		return new Linq<B>(result);
	}
	
	public TInitial First(){
		Iterator<TInitial> iterator = _iterable.iterator();
		
		if(iterator.hasNext()){
			return iterator.next();
		}
		return null;
	}
	
	private TInitial LastList(){
		return ((AbstractList<TInitial>)_iterable).get(
				((AbstractList<TInitial>)_iterable).size()-1);
	}
	
	public TInitial Last(){
		if(extendsAbstractList){
			return LastList();
		}
		
		TInitial result = null;
		
		for(TInitial a : _iterable){
			result = a;
		}
		return result;
	}
	
	private int CountList(){
		return ((AbstractList<TInitial>)_iterable).size();
	}
	
	public int Count(){
		if(extendsAbstractList){
			return CountList();
		}
		
		int count = 0;
		
		for (Iterator<TInitial> iterator = _iterable.iterator(); iterator.hasNext(); iterator.next()) {
			count++;
		}
		
		return count;
	}
	
	public int Count(IFunc<TInitial, Boolean> action){
		return Where(action).CountList();
	}
	
	public Linq<TInitial> Distinct(){
		ArrayList<TInitial> result = new ArrayList<TInitial>();
		HashSet<TInitial> hashSet = new HashSet<TInitial>();
		
		for(TInitial a : _iterable){
			if(!hashSet.contains(a)){
				hashSet.add(a);
				result.add(a);
			}
		}
		 
		return new Linq<TInitial>(result);
	}
	
	public <B extends Comparable<B>> Linq<B> OrderBy(IFunc<TInitial, B> action){
		return OrderBy(action, (a,b) -> a.compareTo(b));
	}
	
	public <B> Linq<B> OrderBy(IFunc<TInitial, B> action, Comparator<B> comparator){
		List<B> result = new ArrayList<B>();
		
		for(TInitial a : _iterable){
			B toAddItem = action.callAction(a); 
			result.add(toAddItem);
		}
		Collections.sort(result, comparator);
		
		return new Linq<B>(result);
	}
	
	public <B extends Comparable<B>> Linq<B> OrderByDescending(IFunc<TInitial, B> action){
		return OrderBy(action, (t1, t2) -> t2.compareTo(t1));
	}
	
	public <B> Linq<B> OrderByDescending(IFunc<TInitial, B> action, Comparator<B> comparator){
		return OrderBy(action, (t1, t2) -> comparator.compare(t2, t1));
	}
	
	public <B> HashMap<B, ArrayList<TInitial>> GroupBy(IFunc<TInitial, B> action){
		HashMap<B, ArrayList<TInitial>> map = new HashMap<>();
		
		for(TInitial item : _iterable){
			B key = action.callAction(item);
			if(!map.containsKey(key)){
				map.put(key, new ArrayList<TInitial>());
			}
			map.get(key).add(item);
		}
		
		return map;
	}
	
	public Linq<TInitial> Reverse(){
		if(extendsAbstractList){
			return ReverseList(); 
		}
		
		LinkedList<TInitial> list = new LinkedList<TInitial>();
		
		for(TInitial a : _iterable){
			list.addFirst(a);
		}
		
		return new Linq<TInitial>(list);
	}
	
	private Linq<TInitial> ReverseList(){
		AbstractList<TInitial> abstractList = (AbstractList<TInitial>)_iterable;
		return new Linq<TInitial>(new Iterable<TInitial>() {

	        @Override
	        public Iterator<TInitial> iterator() {
	            return new Iterator<TInitial>() {

	            	int i = abstractList.size();
	            	
	                @Override
	                public boolean hasNext() {
	                    return i > 0;
	                }

	                @Override
	                public TInitial next() {
	                	i--;
	                    return abstractList.get(i);
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
		});
	}
	
	public void ForEach(IAction<TInitial> action){
		for(TInitial a : _iterable){
			action.callAction(a);
		}
	}

	public Linq<TInitial> Take(int toTakeCount){
		return new Linq<TInitial>(new Iterable<TInitial>() {
			
	        @Override
	        public Iterator<TInitial> iterator() {
	            return new Iterator<TInitial>() {

	            	Iterator<TInitial> iterator = _iterable.iterator();
	            	int i = -1;
	            	
	                @Override
	                public boolean hasNext() {
	                    return iterator.hasNext() && i < toTakeCount-1;
	                }

	                @Override
	                public TInitial next() {
	                	i++;
	                    return iterator.next();
	                }
	            };
	        }
	    });
	}
	
	public Linq<TInitial> Skip(int toSkipCount){
		Iterator<TInitial> iterator = _iterable.iterator();
		for(; toSkipCount > 0; toSkipCount--){
			if(iterator.hasNext()){
				iterator.next();
			}
		}
		
		return new Linq<TInitial>(new Iterable<TInitial>() {
	        
			@Override
	        public Iterator<TInitial> iterator() {
	            return iterator;
	        }
	    });
	}
}
