import java.util.*;

public class ExpressionTree 
{
	/** Root of the expression tree */
	private ExpressionNode root;
	private Deque<Integer> stack;
	
	/** Used to parse a prefix expression */	
	private Scanner strScan;
	
	public ExpressionTree() 
	{
		root = null;
	}
	
	public ExpressionTree(String prefix) 
	{
		root = null;
		setExpression(prefix);
	}

	public void setExpression(String prefix) 
	{
		strScan = new Scanner(prefix);
		String[] arith = strScan.nextLine().split(" ");
		root = insert(arith, new int[] {0});
	}

	private ExpressionNode convert(String arith)
	{
		ExpressionNode current = null;
		for (NodeType t : NodeType.values())
		{
			if (arith.equals(t.getSymbol()))
				current = new ExpressionNode(t, null, null);
		}
		if (arith.matches("\\d+") || arith.matches("-\\d+"))
		{
			current = new ExpressionNode(NodeType.NUMBER, null, null);
			current.setValue(Integer.parseInt(arith));
		}
		return current;
	}

	private ExpressionNode insert(String[] arith, int[] index)
	{
		if (index[0] >= arith.length)
			return null;
		ExpressionNode ex = convert(arith[index[0]]);
		if (!ex.getType().equals(NodeType.NUMBER))
		{
			index[0]++;
			ex.setLeft(insert(arith, index));
			index[0]++;
			ex.setRight(insert(arith, index));
			return ex;
		}
		else return ex;
	}

	public double evaluate() 
	{
		return evaluate(root);
	}

	private double evaluate(ExpressionNode node)
	{
		switch (node.getType())
		{
			case ADD:
				return evaluate(node.getLeft()) + evaluate(node.getRight());
			case SUBTRACT:
				return evaluate(node.getLeft()) - evaluate(node.getRight());
			case MULTIPLY:
				return evaluate(node.getLeft()) * evaluate(node.getRight());
			case DIVIDE:
				return evaluate(node.getLeft()) / evaluate(node.getRight());
			case REMAINDER:
				return evaluate(node.getLeft()) % evaluate(node.getRight());
			case EXPONENT:
				return Math.pow(evaluate(node.getLeft()), evaluate(node.getRight()));
			case NUMBER:
				return node.getValue();
		}
		return 0;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		return inOrder(sb, root).toString();

	}
	private StringBuilder inOrder(StringBuilder out, ExpressionNode node)
	{
		if (node == null)
			return null;
		
		if (node.getLeft() != null || node.getRight() != null)
			out.append("(");
		
		inOrder(out, node.getLeft());
		if (node.getType().equals(NodeType.NUMBER))
			out.append(node.getValue());
		else out.append(" " + node.getType().getSymbol() + " ");
		inOrder(out, node.getRight());

		if (node.getLeft() != null || node.getRight() != null)
			out.append(")");
		
		return out;
	}
}