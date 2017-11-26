package tw.edu.ntust.ee.ee305.motor.monitoring.cloud.math;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

public class PointerMath {

    public static double max(double... m) {
        return Arrays.stream(m).max().getAsDouble();
    }

    public static Double rms(List<Double> value) {
        return Math.sqrt(value.stream().mapToDouble(v -> v * v)
                .average().getAsDouble());
    }

    public static double rms(double[] value) {
        return Math.sqrt(Arrays.stream(value).map(v -> v * v)
                .average().getAsDouble());
    }

    public static Double accvm(List<Double> value) {
        return value.stream().mapToDouble(v -> Math.abs(v)).max().getAsDouble();
    }
    
    public static Double accvm(double[] value) {
        return Arrays.stream(value).map(v -> Math.abs(v)).max().getAsDouble();
    }

    public static Double vpp(List<Double> value) {
        return value.stream().mapToDouble(v -> v).max().getAsDouble() - value.stream().mapToDouble(v -> v).min().getAsDouble();
    }

    public static Double vpp(double[] value) {
        return Arrays.stream(value).map(v -> v).max().getAsDouble() - Arrays.stream(value).map(v -> v).min().getAsDouble();
    }

    public static double[] integral_Math(List<Double> list, double dt) {
DoubleStream.Builder builder = DoubleStream.builder();
list.stream().mapToDouble(d -> d * dt).reduce(0, (a, b) -> {
            a += b;
            builder.add(a);
            return a;
        });
return builder.build().parallel().map(d -> d).toArray();
    }
public static double[] integral_Math(double[] array, double dt) {
DoubleStream.Builder builder = DoubleStream.builder();
Arrays.stream(array).map(d -> d * dt).reduce(0, (a, b) -> {
            a += b;
            builder.add(a);
            return a;
        });
return builder.build().parallel().map(d -> d).toArray();
    }
public static double[] integral(List<Double> list, double dt) {
        double b = Arrays.stream(integral_Math(list, dt)).sum();
        double c = b * dt;
        return Arrays.stream(integral_Math(list, dt)).map(a -> (a - c) * 1000.0).toArray();
    }

    public static double[] integral(double[] array, double dt) {
        double b = Arrays.stream(integral_Math(array, dt)).sum();
        double c = b * dt;
        return Arrays.stream(integral_Math(array, dt)).map(a -> (a - c) * 1000.0).toArray();
    }
public static double UR(double a, double b, double c) {
        double avg, max;
        avg = (a + b + c) / 3;
        if ((Math.abs(a) - Math.abs(avg)) > (Math.abs(b) - Math.abs(avg)) && (Math.abs(a) - Math.abs(avg)) > (Math.abs(c) - Math.abs(avg))) {
            max = a;
        } else if ((Math.abs(b) - Math.abs(avg)) > (Math.abs(a) - Math.abs(avg)) && (Math.abs(b) - Math.abs(avg)) > (Math.abs(c) - Math.abs(avg))) {
            max = b;
        } else {
            max = c;
        }
        max = ((Math.abs(max - avg)) / avg) * 100;
        return max;
    }
public static double THD(double[] a) {
        double th, th2, th3, th4, th5, th6, th7, th8, th9, th10, th11;

        th = max(a[59], a[60], a[61]);
        th2 = max(a[119], a[120], a[121]);
        th3 = max(a[179], a[180], a[181]);
        th4 = max(a[239], a[240], a[241]);
        th5 = max(a[299], a[300], a[301]);
        th6 = max(a[359], a[360], a[361]);
        th7 = max(a[419], a[420], a[421]);
        th8 = max(a[479], a[480], a[481]);
        th9 = max(a[539], a[540], a[541]);
        th10 = max(a[599], a[600], a[601]);
        th11 = max(a[659], a[660], a[661]);
        th = (Math.sqrt((th2 * th2 + th3 * th3 + th4 * th4 + th5 * th5 + th6 * th6
                + th7 * th7 + th8 * th8 + th9 * th9 + th10 * th10 + th11 * th11))) / th;
        return th * 100;
    }
}
