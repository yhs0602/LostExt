package com.kyunggi.lostext;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import android.util.*;

public class MainActivity extends Activity implements Button.OnClickListener
{

	Button btnSelDir;
	Button btnStart;
	EditText editTextProgress;
	EditText editTextPath;
	EditText editTextResult;
	EditText editTextLog;
	File targetDir;
	Thread workerThread;
	String result="done";
	String log="Log";
	private static final int REQUEST_SELECT_FILE = 12345678;

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId())
		{
			case R.id.btnSelDir:
				SelDir();
				break;
			case R.id.btnStart:
				Start();
				break;
		}
	}

	private void SelDir()
	{
		// TODO: Implement this method
		Intent i=new Intent(this, FileSelectorActivity.class);
		startActivityForResult(i, REQUEST_SELECT_FILE);		
	}

	private void Start()
	{
		// TODO: Implement this method
		if(targetDir==null)
		{
			Toast.makeText(this,"please select dir first",3).show();
			return;
		}
		Toast.makeText(this,"Starting",3).show();
		workerThread=new Thread(new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					File [] files=targetDir.listFiles();
					if(files==null)
					{
						return;
					}
					int i=0;
					final int total=files.length;
					for(;i<total;++i)
					{
						try
						{

							Thread.sleep((long)1);
						}
						catch (InterruptedException e)
						{}
						final int iter=i;
						runOnUiThread(new Runnable(){

								@Override
								public void run()
								{
									// TODO: Implement this method
									MainActivity.this.editTextProgress.setText((iter+1)+"/"+total);
								}

							});
						try
						{
							InputStream is=new FileInputStream(files[i]);
							try
							{
								String mimeType = URLConnection.guessContentTypeFromStream(is);
								String extension=reversedHashMap.get(mimeType);
								/*	ContentHandler contenthandler = new BodyContentHandler();
								 Metadata metadata = new Metadata();
								 metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
								 Parser parser = new AutoDetectParser();
								 // OOXMLParser parser = new OOXMLParser();
								 parser.parse(is, contenthandler, metadata);
								 System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
								 System.out.println("Title: " + metadata.get(Metadata.TITLE));
								 System.out.println("Author: " + metadata.get(Metadata.AUTHOR));
								 System.out.println("content: " + contenthandler.toString());
								 */try
								{
									is.close();
								}
								catch(IOException e)
								{
									result+=Log.getStackTraceString(e)+"\n";
								}
								if(extension!=null)
								{
									log+=files[i].getName()+"ext"+(mimeType==null?"":mimeType)+"\n";
									files[i].renameTo(new File(files[i].getAbsolutePath()+"."+extension));

								}else{
									result+=files[i].getName()+"no ext"+(mimeType==null?"":mimeType)+"\n";
								}
							}
							catch (IOException e)
							{
								result+=Log.getStackTraceString(e)+"\n";
							}
						}
						catch (FileNotFoundException e)
						{
							result+=Log.getStackTraceString(e)+"\n";
						}
					}
					runOnUiThread(new Runnable(){

							@Override
							public void run()
							{
								// TODO: Implement this method
								Toast.makeText(MainActivity.this,"done",2).show();
								editTextResult.setText(result);
								editTextLog.setText(log);
							}			
						});

				}	
			});
		workerThread.start();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if(workerThread!=null)
			workerThread.interrupt();
	}

	private static final Map<String, String> fileExtensionMap;
	private static final HashMap<String, String> reversedHashMap = new HashMap<String, String>();


	static {
		fileExtensionMap = new HashMap<String, String>();
		// MS Office
		fileExtensionMap.put("doc", "application/msword");
		fileExtensionMap.put("dot", "application/msword");
		fileExtensionMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		fileExtensionMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
		fileExtensionMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
		fileExtensionMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
		fileExtensionMap.put("xls", "application/vnd.ms-excel");
		fileExtensionMap.put("xlt", "application/vnd.ms-excel");
		fileExtensionMap.put("xla", "application/vnd.ms-excel");
		fileExtensionMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		fileExtensionMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
		fileExtensionMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
		fileExtensionMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
		fileExtensionMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
		fileExtensionMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
		fileExtensionMap.put("ppt", "application/vnd.ms-powerpoint");
		fileExtensionMap.put("pot", "application/vnd.ms-powerpoint");
		fileExtensionMap.put("pps", "application/vnd.ms-powerpoint");
		fileExtensionMap.put("ppa", "application/vnd.ms-powerpoint");
		fileExtensionMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		fileExtensionMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
		fileExtensionMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
		fileExtensionMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
		fileExtensionMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
		fileExtensionMap.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
		fileExtensionMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
		// Open Office
		fileExtensionMap.put("odt", "application/vnd.oasis.opendocument.text");
		fileExtensionMap.put("ott", "application/vnd.oasis.opendocument.text-template");
		fileExtensionMap.put("oth", "application/vnd.oasis.opendocument.text-web");
		fileExtensionMap.put("odm", "application/vnd.oasis.opendocument.text-master");
		fileExtensionMap.put("odg", "application/vnd.oasis.opendocument.graphics");
		fileExtensionMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
		fileExtensionMap.put("odp", "application/vnd.oasis.opendocument.presentation");
		fileExtensionMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
		fileExtensionMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		fileExtensionMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
		fileExtensionMap.put("odc", "application/vnd.oasis.opendocument.chart");
		fileExtensionMap.put("odf", "application/vnd.oasis.opendocument.formula");
		fileExtensionMap.put("odb", "application/vnd.oasis.opendocument.database");
		fileExtensionMap.put("odi", "application/vnd.oasis.opendocument.image");
		fileExtensionMap.put("oxt", "application/vnd.openofficeorg.extension");
		// Other
		fileExtensionMap.put("txt", "text/plain");
		fileExtensionMap.put("rtf", "application/rtf");
		fileExtensionMap.put("pdf", "application/pdf");
		fileExtensionMap.put("jpg", "image/jpeg");
		fileExtensionMap.put("jpg", "image/x-citrix-jpeg");
		fileExtensionMap.put("png", "image/png");
		fileExtensionMap.put("png", "image/x-citrix-png");
		fileExtensionMap.put("png", "image/x-png");


		for (String i : fileExtensionMap.keySet()) {
			reversedHashMap.put(fileExtensionMap.get(i), i);
		}
	}

	private static final FileExtensionMap[] extmap=new FileExtensionMap[]{
		new FileExtensionMap(
			"ai",
			"25504446",
			0
		),
		new FileExtensionMap(
			"avi",
			"52494646",
			0
		),
		new FileExtensionMap(
			"avi",
			"415649204C495354",
			0
		),
		new FileExtensionMap(
			"gif",
			"474946383761",
			0
		),
		new FileExtensionMap(
			"gif",
			"474946383961",
			0
		),
		new FileExtensionMap(
			"jpg",
			"FFD8FFE0",
			0
		),
		new FileExtensionMap(
			"jpg",
			"494600",
			0
		),
		new FileExtensionMap(
			"jpg",
			"FFD8FFE1",
			0
		),
		new FileExtensionMap(
			"mov",
			"0000001466747970",
			0
		),
		new FileExtensionMap(
			"mov",
			"6D6F6F76",
			0
		),
		new FileExtensionMap(
			"mov",
			"71742020",
			4
		),
		new FileExtensionMap(
			"mov",
			"66726565",
			4
		),
		new FileExtensionMap(
			"mov",
			"6D646174",
			4
		),
		new FileExtensionMap(
			"mov",
			"77696465",
			4
		),
		new FileExtensionMap(
			"mov",
			"706E6F74",
			4
		),
		new FileExtensionMap(
			"mov",
			"736B6970",
			4
		),
		new FileExtensionMap(
			"mp3",
			"FFFB",
			0
		),
		new FileExtensionMap(
			"mp3",
			"494433",
			0
		),
		new FileExtensionMap(
			"mp4",
			"0000001866747970",
			0
		),
		new FileExtensionMap(
			"mp4",
			"33677035",
			0
		),
		new FileExtensionMap(
			"pdf",
			"25504446", // <-- NOTE: Same as AI.
			0
		),
		new FileExtensionMap(
			"png",
			"89504E470D0A1A0A",
			0
		),
		new FileExtensionMap(
			"psd",
			"4F676753",
			0
		),
		new FileExtensionMap(
			"psd",
			"38425053",
			0
		),
		new FileExtensionMap(
			"xlsx",
			"504B0304", // <-- NOTE: Same as ZIP.
			0
		),
		new FileExtensionMap(
			"zip",
			"504B0304",
			0
		),
		new FileExtensionMap(
			"zip",
			"504B0506",
			0
		),
		new FileExtensionMap(
			"zip",
			"504B0708",
			0
		),
		new FileExtensionMap(
			"zip",
			"504B4C495445",
			30
		),
		new FileExtensionMap(
			"zip",
			"504B537058",
			526
		),
		new FileExtensionMap(
			"zip",
			"57696E5A6970",
			29
		),
		new FileExtensionMap(
			"zip",
			"57696E5A6970",
			152
		),
		new FileExtensionMap(
			"zip",
			"1F8B08",
			0
		)
	};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		btnStart=(Button) findViewById(R.id.btnStart);
		btnSelDir=(Button) findViewById(R.id.btnSelDir);
		editTextPath=(EditText) findViewById(R.id.editTextPath);
		editTextProgress=(EditText) findViewById(R.id.editTextProgress);
		editTextResult=(EditText) findViewById(R.id.editTextResult);
		editTextLog=(EditText) findViewById(R.id.editTextLog);
		btnStart.setOnClickListener(this);
		btnSelDir.setOnClickListener(this);
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_SELECT_FILE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String path=data.getStringExtra("com.kyunggi.lostext.path");
				File file=new File(path);
				file=file.getParentFile();
				editTextPath.setText(file.getAbsolutePath());
				targetDir=file;
			}
		}
	} 

}
