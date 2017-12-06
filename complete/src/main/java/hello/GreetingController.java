package hello;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
//import hello.PointerMath;

@RestController
public class GreetingController {

    //private static final String template = "Hello, %s!";
    //private final AtomicLong counter = new AtomicLong();
    private String nextInterface = null;

    /*
    private static Logger logger = Logger.getLogger(GreetingController.class);
    private static void log4jProperties() {
	PropertyConfigurator.configure("log4j.properties");
    }
    */


    @RequestMapping(value="/motorclassifier-c", method = RequestMethod.POST)
    //public Greeting greeting(@RequestParam(value="name", defaultValue="World") InputStream requestStream) {
    public String greeting(InputStream requestBodyStream) {
	String name = null;
	String returnResult = null;
	//System.out.print("Starting Point");

	try{
		//catch input string
		JSONObject jsonInput = new JSONObject(this.convertRequestBODY2String(requestBodyStream));
		returnResult = jsonInput.toString();
		double[] DiagnosisPointer = new double[5];
		System.out.print("Request body:" + returnResult);
		String stringFirehose = "firehoseData";
		String stringFeature = "featureData";


		String machineID = jsonInput.getJSONObject(stringFirehose).get("Machine_ID").toString();;
		JSONArray Va = jsonInput.getJSONObject(stringFirehose).getJSONArray("V1");
                JSONArray Vb = jsonInput.getJSONObject(stringFirehose).getJSONArray("V2");
                JSONArray Vc = jsonInput.getJSONObject(stringFirehose).getJSONArray("V3");
                JSONArray Ia = jsonInput.getJSONObject(stringFirehose).getJSONArray("I1");
                JSONArray Ib = jsonInput.getJSONObject(stringFirehose).getJSONArray("I2");
                JSONArray Ic = jsonInput.getJSONObject(stringFirehose).getJSONArray("I3");
                JSONArray AccX = jsonInput.getJSONObject(stringFirehose).getJSONArray("A1");
                JSONArray AccY = jsonInput.getJSONObject(stringFirehose).getJSONArray("A2");
                JSONArray AccZ = jsonInput.getJSONObject(stringFirehose).getJSONArray("A3");
 
                JSONArray Va_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_V1");
                JSONArray Vb_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_V2");
                JSONArray Vc_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_V3");
                JSONArray Ia_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_I1");
                JSONArray Ib_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_I2");
                JSONArray Ic_FFT = jsonInput.getJSONObject(stringFeature).getJSONArray("list_I3");

		//prepare classifier input
		List<Double> V1 = new ArrayList<Double>();
		List<Double> V2 = new ArrayList<Double>();
                List<Double> V3 = new ArrayList<Double>();
                List<Double> I1 = new ArrayList<Double>();
                List<Double> I2 = new ArrayList<Double>();
                List<Double> I3 = new ArrayList<Double>();
                List<Double> A1 = new ArrayList<Double>();
                List<Double> A2 = new ArrayList<Double>();
                List<Double> A3 = new ArrayList<Double>();

		for (int i=0; i<Va.length(); i++) {
			V1.add( Double.parseDouble(Va.get(i).toString()) );
			V2.add( Double.parseDouble(Vb.get(i).toString()) );
                    	V3.add( Double.parseDouble(Vc.get(i).toString()) );
                    	I1.add( Double.parseDouble(Ia.get(i).toString()) );
                    	I2.add( Double.parseDouble(Ib.get(i).toString()) );
                    	I3.add( Double.parseDouble(Ic.get(i).toString()) );
                    	A1.add( Double.parseDouble(AccX.get(i).toString()) );
                    	A2.add( Double.parseDouble(AccY.get(i).toString()) );
                    	A3.add( Double.parseDouble(AccZ.get(i).toString()) );
		}

		//FFT
		List<Double> V1_FFT = new ArrayList<Double>();
                List<Double> V2_FFT = new ArrayList<Double>();
                List<Double> V3_FFT = new ArrayList<Double>();
                List<Double> I1_FFT = new ArrayList<Double>();
                List<Double> I2_FFT = new ArrayList<Double>();
                List<Double> I3_FFT = new ArrayList<Double>();

		for (int i=0; i<Va_FFT.length(); i++) {
                        V1_FFT.add( Double.parseDouble(Va_FFT.get(i).toString()) );
                        V2_FFT.add( Double.parseDouble(Vb_FFT.get(i).toString()) );
                        V3_FFT.add( Double.parseDouble(Vc_FFT.get(i).toString()) );
                        I1_FFT.add( Double.parseDouble(Ia_FFT.get(i).toString()) );
                        I2_FFT.add( Double.parseDouble(Ib_FFT.get(i).toString()) );
                        I3_FFT.add( Double.parseDouble(Ic_FFT.get(i).toString()) );
                }


		List<Integer> cmsList = new ArrayList<Integer>();
		cmsList.add(25);
		cmsList.add(50);
		cmsList.add(75);
		cmsList.add(100);
		
		Random r = new Random();
		//int cmsValue = cmsList.get(r.nextInt(cmsList.size()));

		//create output JSONObject	
		JSONObject json = new JSONObject();
                JSONObject json2 = new JSONObject();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // the format of your date
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String formattedDate = sdf.format(date);

		json.put("ID", machineID);
                json.put("timestamp", formattedDate);
		json2.put("Va_rms", PointerMath.rms(V1).intValue());
		json2.put("Vb_rms", PointerMath.rms(V2).intValue());
                json2.put("Vc_rms", PointerMath.rms(V3).intValue());
                json2.put("Ia_rms", PointerMath.rms(I1).intValue());
                json2.put("Ib_rms", PointerMath.rms(I2).intValue());
                json2.put("Ic_rms", PointerMath.rms(I3).intValue());
                json2.put("VUR", PointerMath.UR(PointerMath.rms(V1), PointerMath.rms(V2), PointerMath.rms(V3).intValue()));
                json2.put("IUR", PointerMath.UR(PointerMath.rms(I1), PointerMath.rms(I2), PointerMath.rms(I3).intValue()));

                json2.put("VTHD", PointerMath.max(PointerMath.THD(V1_FFT.stream().mapToDouble(d -> d).toArray()), 
						PointerMath.THD(V2_FFT.stream().mapToDouble(d -> d).toArray()), 
						PointerMath.THD(V2_FFT.stream().mapToDouble(d -> d).toArray())));

                //json2.put("VTHD", 32);
                json2.put("ITHD", PointerMath.max(PointerMath.THD(I1_FFT.stream().mapToDouble(d -> d).toArray()), 
					PointerMath.THD(I2_FFT.stream().mapToDouble(d -> d).toArray()), 
					PointerMath.THD(I2_FFT.stream().mapToDouble(d -> d).toArray())));
                //json2.put("ITHD", 23);
                json2.put("AccX_max", PointerMath.accvm(A1).intValue());
                json2.put("AccY_max", PointerMath.accvm(A2).intValue());
                json2.put("AccZ_max", PointerMath.accvm(A3).intValue());
                json2.put("VelX_max", PointerMath.rms(PointerMath.integral(A1, A1.size())));
                json2.put("VelY_max", PointerMath.rms(PointerMath.integral(A2, A2.size())));
                json2.put("VelZ_max", PointerMath.rms(PointerMath.integral(A3, A3.size())));
                json2.put("DisX_max", PointerMath.vpp(PointerMath.integral(PointerMath.integral(A1, A1.size()), A1.size())).intValue());
                json2.put("DisY_max", PointerMath.vpp(PointerMath.integral(PointerMath.integral(A2, A2.size()), A2.size())).intValue());
                json2.put("DisZ_max", PointerMath.vpp(PointerMath.integral(PointerMath.integral(A3, A3.size()), A3.size())).intValue());
                //json2.put("CMS", cmsList.get(r.nextInt(cmsList.size())));
                //
		if (machineID.equals("Motor_Linkou2")){
			json2.put("CMS", 50);
		} else if (machineID.equals("Motor_AIPSR_Lab2")) {
			json2.put("CMS", 100);
		} else {
			json2.put("CMS", MotorCMS.MotorHealth(PointerMath.max(PointerMath.rms(PointerMath.integral(A1, A1.size())), 
							PointerMath.rms(PointerMath.integral(A2, A2.size())), 
							PointerMath.rms(PointerMath.integral(A3, A3.size()))), 
							PointerMath.UR(PointerMath.rms(I1), PointerMath.rms(I2), PointerMath.rms(I3)), 
							PointerMath.max(PointerMath.THD(I1_FFT.stream().mapToDouble(d -> d).toArray()), 
							PointerMath.THD(I2_FFT.stream().mapToDouble(d -> d).toArray()), 
							PointerMath.THD(I2_FFT.stream().mapToDouble(d -> d).toArray()))));

		}


		DiagnosisPointer = MotorCMS.FaultDiagnosis(PointerMath.UR(PointerMath.rms(I1), 
									PointerMath.rms(I2), 
									PointerMath.rms(I3)), 
									PointerMath.max(PointerMath.rms(PointerMath.integral(A1, A1.size())), 
													PointerMath.rms(PointerMath.integral(A2, A2.size())), 
													PointerMath.rms(PointerMath.integral(A3, A3.size()))), 
													PointerMath.max(PointerMath.accvm(A1), 
													PointerMath.accvm(A2), 
													PointerMath.accvm(A3)));

                json2.put("Rotor", DiagnosisPointer[1]);
                json2.put("Stator", DiagnosisPointer[2]);
                json2.put("Bearing", DiagnosisPointer[3]);
                json2.put("Eccentric", DiagnosisPointer[4]);
                json.put("detail", json2);
                StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);

		//post to elasticSearch
		if (nextInterface.equals("visualization")){
			System.out.print("post to elasticSearch");
			HttpClient httpClient = HttpClientBuilder.create().build();
	                HttpPost request = new HttpPost("http://124.9.14.16:9200/testindex1/mytype");
	                //HttpPost request = new HttpPost("http://192.168.0.23:9200/testindex1/mytype");
	                request.setEntity(entity);
	                HttpResponse response = httpClient.execute(request);
			returnResult = json.toString();
		} else {
			returnResult = "NextInterface is null";
		}

	} catch (Exception ex) {
		System.out.print("JSON Execption");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
        }   
	return returnResult;
    }



	@RequestMapping(value="/config", method = RequestMethod.POST)
	public String getConfig(InputStream requestBodyStream) {
		//log4jProperties();

		try{
			JSONObject jsonInput = new JSONObject(this.convertRequestBODY2String(requestBodyStream));
			nextInterface = jsonInput.get("Next_Interface").toString();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
	                PrintWriter pw = new PrintWriter(sw);
	                ex.printStackTrace(pw);
	                return sw.toString();
		}
		//logger.info(nextInterface);
		return nextInterface;
	}



	private String convertRequestBODY2String(InputStream requestBodyStream){
    		StringBuffer buffer = new StringBuffer();
    		int bufferContent = 0;
    		do
    			{
     			try {
      				bufferContent = requestBodyStream.read();
      				//System.out.println(bufferContent);
      				if(bufferContent > 0)
       				buffer.append((char) bufferContent);
        
     			} catch (IOException e) {
				e.printStackTrace();
    			 }
    			}while(bufferContent > 0 );
			System.out.println(buffer.toString());
 		   return buffer.toString();
		 }

}
