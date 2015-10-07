
public interface IAction <TParam, TResult> {
	public TResult callAction(TParam parameter);
}