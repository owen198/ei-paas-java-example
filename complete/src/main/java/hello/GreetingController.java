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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
//import hello.PointerMath;

@RestController
public class GreetingController {

    //private static final String template = "Hello, %s!";
    //private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/motorclassifier", method = RequestMethod.POST)
    //public Greeting greeting(@RequestParam(value="name", defaultValue="World") InputStream requestStream) {
    public String greeting(InputStream requestBodyStream) {
	String name = null;
	String returnResult = null;
	//System.out.print("Starting Point");

	try{
		//catch input string
		JSONObject jsonInput = new JSONObject(this.convertRequestBODY2String(requestBodyStream));
		returnResult = jsonInput.toString();
		System.out.print("Request body:" + returnResult);
		String stringFirehose = "firehoseData";
		String stringFeature = "featureData";

/*
		JSONArray Va = jsonInput.getJSONObject(stringFirehose).getJSONArray("V1");
                JSONArray Vb = jsonInput.getJSONObject(stringFirehose).getJSONArray("V2");
                JSONArray Vc = jsonInput.getJSONObject(stringFirehose).getJSONArray("V3");
                JSONArray Ia = jsonInput.getJSONObject(stringFirehose).getJSONArray("I1");
                JSONArray Ib = jsonInput.getJSONObject(stringFirehose).getJSONArray("I2");
                JSONArray Ic = jsonInput.getJSONObject(stringFirehose).getJSONArray("I3");
                JSONArray AccX = jsonInput.getJSONObject(stringFirehose).getJSONArray("A1");
                JSONArray AccY = jsonInput.getJSONObject(stringFirehose).getJSONArray("A2");
                JSONArray AccZ = jsonInput.getJSONObject(stringFirehose).getJSONArray("A3");
*/ 
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
/*
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
*/




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

		json.put("ID", "Motor2");
                json.put("timestamp", formattedDate);
		json2.put("Va_rms", 221.0);
		json2.put("Vb_rms", 221.0);
                json2.put("Vc_rms", 220.0);
                json2.put("Ia_rms", 5.0);
                json2.put("Ib_rms", 6.0);
                json2.put("Ic_rms", 7.0);
                json2.put("VUR", 5.0);
                json2.put("IUR", 6.0);
                json2.put("VTHD", 3.0);
                json2.put("ITHD", 5.0);
                json2.put("AccX_max", 5.0);
                json2.put("AccY_max", 5.0);
                json2.put("AccZ_max", 54.0);
                json2.put("VelX_max", 5.0);
                json2.put("VelY_max", 9.0);
                json2.put("VelZ_max", 5.0);
                json2.put("DisX_max", 5.0);
                json2.put("DisY_max", 2.0);
                json2.put("DisZ_max", 50.0);
                json2.put("CMS", cmsList.get(r.nextInt(cmsList.size())));
                json2.put("Rotor", 5.0);
                json2.put("Stator", 5.0);
                json2.put("Bearing", 5.0);
                json2.put("Eccentric", 5.0);
                json.put("detail", json2);
                StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);

		//post to elasticSearch
		System.out.print("post to elasticSearch");
		HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("http://124.9.14.16:9200/testindex1/mytype");
                request.setEntity(entity);
                HttpResponse response = httpClient.execute(request);


	} catch (Exception ex) {
		System.out.print("JSON Execption");

		//JSONObject jsonInput = new JSONObject(this.convertRequestBODY2String(requestBodyStream));
                //returnResult = jsonInput.toString();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		//return returnResult;
		return sw.toString();
        }   
	return returnResult;
//	return this.convertRequestBODY2String(requestBodyStream);
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
