package hello.roommate.recommendation.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SimilarityUtils {
	public double getNorm(double[] x) {
		double sum = 0;
		for (double v : x) {
			sum += v * v;
		}
		return Math.sqrt(sum);
	}

	public double dotProduct(double[] a, double[] b) {
		double sum = 0;
		int length = a.length;
		for (int i = 0; i < length; i++) {
			sum += (a[i] * b[i]);
		}

		return sum;
	}

	public double cosSimilarity(double[] a, double[] b) {
		double normA = getNorm(a);
		double normB = getNorm(b);
		return dotProduct(a, b) / (normA * normB);
	}

	public static double[] getVec(Map<String, List<Long>> requestLifeStyleMap,
		Map<Long, Integer> opionIdxMap) {
		int size = opionIdxMap.size();
		double[] vec = new double[size];
		for (String key : requestLifeStyleMap.keySet()) {
			List<Long> values = requestLifeStyleMap.get(key);
			double d = Math.sqrt(values.size());
			for (Long value : values) {
				int idx = opionIdxMap.get(value);
				vec[idx] = 1 / d;
			}
		}
		return vec;
	}
}
