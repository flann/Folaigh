using System;
using System.IO;

namespace org.karmashave.folaigh.test
{
	/// <summary>
	/// Summary description for FolaighTestUtils.
	/// </summary>
	public class FolaighTestUtils
	{
		private static string[] encodedKeyStore = {
													  "MIACAQMwgAYJKoZIhvcNAQcBoIAkgASCBjwwgDCABgk=",
													  "KoZIhvcNAQcBoIAkgASCBiQwggYgMIIDDQYLKoZIhvc=",
													  "DQEMCgECoIICsjCCAq4wKAYKKoZIhvcNAQwBAzAaBBQ=",
													  "HrV1jEG1vPse5UzDHS0dVAXBAoUCAgQABIICgG4hRL4=",
													  "QsdRpdCl1OkTYdeB+QdDxQ3b8hQyAP2QauBu7F7y/1Y=",
													  "mcLFnZKMUSebq+wCWDGK+99CbV54V4eLCJn/p+U/FiY=",
													  "CviHk8x6ftJyZYCIaBf5TZMJaftVyW4HSDB/ClYNLsU=",
													  "LrbI2SS/ABuDfuqjTuyH06CVhTc2xTBhgUJp4Ot/84Q=",
													  "5m5TbyS1HJkIypvffdSXxbn9wCyV3NcCdm7ltDT6Wr8=",
													  "ZQVoXQ8z4jgJblcIx/lMQT684GW+8SlzkPQLWvQZSQQ=",
													  "MR/jO61qLx7Z4dFEwcKTUeOF4evF0Ke8et4hZtoFlA8=",
													  "luxP1LT3oJ4WsIJ8Xd0EAEvtxw6kf5TuqP9WQ8hd2nY=",
													  "r1eoesdrB1Guum2XL8/Z7TWEXpcdop98QRcMkAEQtgU=",
													  "OuQKs+qeUIEp40uMIV2F7SMh1VlnQoMLdtnmuLdJyFI=",
													  "KgqD7DoA+63mLfTmg8oyatCvfI1P2+CNU4mQBq1Fcj4=",
													  "7TQs4UPkEJcYUSajy9kHFUsG0qaYc2NY5HQZgpX5GsU=",
													  "ft1zPlKIhOpeZWRtp+eshrzQ9I9G5mQzv+woqsGl5c4=",
													  "BzbPt+yENSb4jVW4xflteCHsNz9hhSGHLpvrhjwRP9M=",
													  "jgIylfu0JE0ZFtIJEgs59RWEiRslEHSlEYuNcUJxEoc=",
													  "ulFTdrLA5W2S/dZIUka7HbbIB73OWvz6P8nw+55VXdM=",
													  "MV47HhQm/MFGRjvBIVxznJtIFwfesJ/Z77HSAsjsuSc=",
													  "sZQ2O1fOHAYfJE53kq1NBDnxcXFEPr1L2++pVx66/Ho=",
													  "JDX3LIHl/YDJhRIyhX4z2uJ/MmXDTzO34Iqt+oqdqkc=",
													  "wppIhAaObtVkBJuCEoSciDgX9FLWBpjPd4u2BTFIMCE=",
													  "BgkqhkiG9w0BCRQxFB4SAGMAbwB1AG4AdAB5AEsAZQA=",
													  "eTAjBgkqhkiG9w0BCRUxFgQULFzM9J/ZIRVa5cOm6oI=",
													  "c5IoolzfMIIDCwYLKoZIhvcNAQwKAQKgggKyMIICrjA=",
													  "KAYKKoZIhvcNAQwBAzAaBBSXvnQ6GxffVog4jIqVAl4=",
													  "ZTnXOSYCAgQABIICgArLEmuQSeCwjmn/eZk/1DIn0G0=",
													  "f15nEoPHAAcLoHwFTLwxxacQMDyY4TJFIw5Lo2tWGJA=",
													  "XL7YPhTYJS9T+i8l3Hr554dQIZ8DQ9UrKlzgGADceVk=",
													  "lnri0D/Zgy1XQL2UnMm5Adeihn1pxnw3HKhpWlfA0LA=",
													  "AoQMlRVO9wYZvIqYuhnaq2tKaQQAoMS8PgqT9z5x6cY=",
													  "YLlytb0v1/c3X1f67VS7XiHGuvI/id8MhRouOosjDww=",
													  "rFDdid79zrZRQIwUH5a32qN8/SXOEUTmBBFghqYtjMg=",
													  "R0C7gluujWF1l2CJt7RHLOh/9+ccWcbTOWWU9eF+q+0=",
													  "DdVvUu0Rh/UsREtT3PNQczTRf2KV6ZT6STQWdmb5P5A=",
													  "dQhbs3h9/FygaAww+tfUzgAbhZGR8IfwItebLf0W1Yg=",
													  "3uJJC0ss38ocRQdnqcDIFc/53Wy+enu7b1rZL1hk2LQ=",
													  "bHeYSoDRxCsx52xSCIsZlE4FgAEtonFu9yjV4NFhPQc=",
													  "ciKqNgnPnacO3B13UTaDd/XstyBeZN6MZaxb5dz34O4=",
													  "lS9JhSGrWjkS3/D7qafJG7YbaXFubZ10BBC4LWb+jc4=",
													  "XkD+2/JmfusFiOIbDQTgi/ZveY1RC3qJzipX5rTPdwc=",
													  "O2S7w4BmxM87ICxcCZa0bfiT+VRCWNtwlpvB29hg2Dg=",
													  "l1T11/x4/k92X5giVlOp075BXE6P7OFS7vKIZe9kxu8=",
													  "ynGexyJfs9IANtF595abPXTCtFZnoQsPi8y4VF83id8=",
													  "xBCIuMzAyP+A7Y9cKuPxWqi4YEo7ZcUPtnlF99JE91g=",
													  "kZTlYTluW/iCoyi59plHvSeWKhRBJiV0doZw/LGW1ZQ=",
													  "WDUafHhC4f1XiAB9LjFGMB8GCSqGSIb3DQEJFDESHhA=",
													  "AHMAdABhAHQAZQBLAGUAeTAjBgkqhkiG9w0BCRUxFgQ=",
													  "FPyxWrl2RGMN5yGplPKFym2cRGGKAAQBAAQBAAQBAAQ=",
													  "AQAEggYzADCABgkqhkiG9w0BBwaggDCAAgEAMIAGCSo=",
													  "hkiG9w0BBwEwKAYKKoZIhvcNAQwBBjAaBBSSdSeBfNY=",
													  "GAfSmimEbm5NEq/s8z8CAgQAoIAEggXgcyt2BCH0EQk=",
													  "JS2zBCSMwoCMQuKNcF8JzjhOJZ9Ru6YSPjupteIIkpQ=",
													  "2Q+MX3VpW1a0Asn/V5fMGYWkNbmbaGtpLvId94lhrQs=",
													  "60FhAD83aTnJmNFqNs2fF1dlYzSX+22uCgy50NMcyKg=",
													  "MU4jE7UfNomOCsURJPogpMN8IZ3xpqB0VDdick5SEn4=",
													  "7z3Z2DVzqrzb1GAD/Q86A/IEsNaKEJQVFU/LhLhmdZk=",
													  "wcgoHSUCI5eEK8KuBPqapHmg4H+Q5PUrOWSFeLh5CWI=",
													  "rqk2eCYZh0NAHWJgj/oeRdx6zETiHy7Pv+bCpampE9I=",
													  "x4XzpiT90hWlU2ohfBKsZy2Z1wUQ3WV8235nyyDa8LA=",
													  "b/zmOakuF/GEH4udsuRf4ExkG94jQvr6g/AmobUgWCY=",
													  "Ft9PAZqADyquZvZo9kQmeLCDr7e/26nKFSdUcLzSMms=",
													  "MqTE/lgTeGRuG9vvZZLk+mNIa5s2gAtD9uhNuiolO5Y=",
													  "M57c4L4l2wOsJ8HV/BqQLg+nC18dqEPexkg7weFh2zs=",
													  "gnm12V8AiLjbUqGF7xVzLd+MhOLKDK045mQZpIg+nFk=",
													  "pxg1U0/5F8SDq3IIClrh1d0MHIxtnabx07DQOZh/7Ck=",
													  "Qsyd8PRDDDzMaZNEmsPcaBGNQgiIcoJbLiNkpAG9dSY=",
													  "lkakqVEcobWbrGVr2UE2o8B/bXMLAdejg48X/cjcNfk=",
													  "h2AVJWQ7/+FQFO27ySHnFwzKKg1fA22P96xiocbVAmU=",
													  "wm9nA3oZ2jntw1OhScofwuvQ0JeKLF/Co0ArhRPk7V0=",
													  "NBeWesj7gyfHu5tCl4LLowRw3RjqEtSGfs67ETQ0XjI=",
													  "LPj0IBQ5sz+iEZxfHMdMNyndoPGcMrCpf/bCObm24IE=",
													  "j12wqKKNLm9WICyHPZwb3/bafpc/xO/A0RPVnex8aaI=",
													  "xlpKAhdNXpvHhvkrFHOecfTFsTE7Zi9XC0alt3aoD5U=",
													  "rhl13oBgNNlU7zIQAwHmurPVfw92HQvfld1diDiJB1I=",
													  "ItBn6gaAoin6601Iw/cBPAmM3f/fXF4DX1JbZEVnf64=",
													  "TCxruxtQXrKT4Q+A2yDCtdV1HHLw+tQtH+VmAIvhuR0=",
													  "D42UKnSe60Ca4Pfm2pzimjPIY0O6Mdcbv1rPX4HUrhU=",
													  "rgwaqXsEQ1vT1Ih92qss3mMkNid84/R59Z4N2LbFVss=",
													  "vb9zEmTXAHZpjYWzdCIdGwjFgWOMzV+hMjEoWUj+hNs=",
													  "/K5H5q0S/R6FqscYSYQFSYDct8+2JEMdaH9rQBVIV1I=",
													  "8CcFJjNt4QfW1FXqBLFgRfh2uVlCW3dTYI6BQHymsoc=",
													  "+ge26x/k/jzXX6KJN5SaeMm6ADLk25FdVGKXhVue/rU=",
													  "MR+8s9BM4su2jfqimfvwHNuO3akQ3Owsf2FUFzvrjF0=",
													  "mOlxFKb0EsEul4lJGZYLUgM3efTwgxRzMaes8Ev9+rE=",
													  "ir684RwcELm3ZZPeOrQV5yBLrwpRl00oz2eAi+7OyOc=",
													  "HilTcb2VZoPyBB3v66EIvPji8V6OqO431T1sneK5YkE=",
													  "8C71MOe5CMGyE/UTyv+VTXnMITq97t0GdYrNx6RVn3w=",
													  "jyIoaupA6mQBx+OINJUeii6qtIgQDuQALKPS+UZaoBU=",
													  "4n+VIXQLzZvePf+vJCcAlFwsduJQwj+78UeIdlCbVsg=",
													  "KzQ4GrHnCvrH15kny6RQAfdzTXyeT98F+p/BZQTbDvw=",
													  "/Q6itV1hMyZPTpB2K5ke4OokcZHKuMBeFXk1LzgEMFA=",
													  "2ASogEym6X2exlqcPMIvx3rHjfUEp250F9+QXUcx3q8=",
													  "FnXKNfxwJLxN4ZTsYwKcFPSt2OKI/UvAN6VT28OkxME=",
													  "kxCqhySnqpXjPMWZANWvvo9zDqtETXlsYcpQPxPOXuE=",
													  "SY+ItWpiWW5HmW5ZUTDx4zXMtIelBqPVSbdOcUXZB4w=",
													  "UpbtzgsX2v5d5N6xci/Fc939JrfqdNnVLy01wwYWppA=",
													  "MObmZyVe9sW2rPCPJ6veRHO4ln45YQvmXNIToq1jjyw=",
													  "xrAvp3/RgxT4MLHYdzV0gBGANk8iCllyAAQBAAQBAAQ=",
													  "AQAEAQAEAQAEAQAEAQAEAQAEAQAEAQAEAQAAAAAAAAA=",
													  "MD0wITAJBgUrDgMCGgUABBSrpMMTx7mRILA20NEyZnE=",
													  "vdY9+KwEFF4YaNUfwpNtWgi2oMwYQmLGkRPzAgIEAAA=", "AA==" };

		/// <summary>
		/// Get a memory stream containing a key store
		/// </summary>
		/// <returns>a memory stream containing a PKCS12 keystore</returns>
		public static Stream getKeyStoreStream() 
		{
			MemoryStream output = new MemoryStream();
			for (int i = 0; i < encodedKeyStore.Length; i++) 
			{
				byte[] buffer = Convert.FromBase64String(encodedKeyStore[i]);
				output.Write(buffer,0,buffer.Length);
			}
			output.Close();
			return new MemoryStream(output.ToArray());
		}
		
		/// <summary>
		/// Write a key store to a file in the temporary directory
		/// </summary>
		/// <param name="baseFilename">the filename to write</param>
		/// <returns>The full path of the file</returns>
		public static String writeKeyStoreToFile(String baseFilename)
		{
			Stream input = getKeyStoreStream();
			string path = Path.GetTempPath() + "\\" + baseFilename;
			FileStream output = new FileStream(path,FileMode.Create);
			byte[] buffer = new byte[32];
			int len = 0;
			while ((len = input.Read(buffer,0,32)) > 0) 
			{
				output.Write(buffer,0,len);
			}
			input.Close();
			output.Close();
			return path;
		}
	}
}
