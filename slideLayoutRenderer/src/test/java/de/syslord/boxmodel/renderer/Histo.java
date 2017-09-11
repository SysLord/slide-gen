package de.syslord.boxmodel.renderer;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.collect.Maps;

public class Histo {

	HashMap<Object, Long> m = Maps.newHashMap();

	double[] sum = new double[4];

	public void add(int[] pixel) {
		Long val = m.containsKey(pixel) ? m.get(pixel) : 0;
		m.put(pixel, val);

		// böstimmt!
		sum[0] += pixel[0];
		sum[1] += pixel[1];
		sum[2] += pixel[2];
	}

	public int[] getPeak() {
		Optional<Entry<Object, Long>> max = m.entrySet().stream().max((x, y) -> Long.compare(x.getValue(), y.getValue()));
		return (int[]) max.get().getKey();
	}

	public double[] getSum() {
		return sum;
	}

}
