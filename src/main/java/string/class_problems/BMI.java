import java.util.Locale;
import java.util.Random;
public class BMI
{
	public static void main(String[] args)
	{
		double[] heights = new double[10];
		double[] weights = new double[10];
		Random random = new Random();
		for (int i = 0; i < heights.length; i++)
		{
			heights[i] = 1.50 + random.nextDouble() * 0.40;
			weights[i] = 50 + random.nextDouble() * 50;
		}
		printWellnessReport(heights, weights);
	}
	static String getBmiStatus(double bmi)
	{
		if (bmi < 18.5)
		{
			return "Underweight";
		}
		if (bmi < 25.0)
		{
			return "Normal";
		}
		if (bmi < 30.0)
		{
			return "Overweight";
		}
		return "Obese";
	}
	static void printWellnessReport(double[] heights, double[] weights)
	{
		if (heights.length != weights.length)
		{
			throw new IllegalArgumentException("Heights and weights must have the same length.");
		}
		System.out.printf(Locale.US, "%-10s %-14s %-14s %-10s %s%n",
				"Person", "Height (m)", "Weight (kg)", "BMI", "Status");
		System.out.println("--------------------------------------------------------------");
		for (int i = 0; i < heights.length; i++)
		{
			double bmi = weights[i] / (heights[i] * heights[i]);
			System.out.printf(Locale.US, "%-10d %-14.2f %-14.2f %-10.2f %s%n",
					i + 1, heights[i], weights[i], bmi, getBmiStatus(bmi));
		}
	}
}
