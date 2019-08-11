package VidaAutomation.VidaAutomation.utilities;

import java.util.Locale;

public interface BaseTest {

	public final long PAGE_LOAD_TIMEOUT = 120; 	// in seconds; occasional slow loads in test
	public final long JS_EXECUTION_TIMEOUT = 30; 	// in seconds
	public final int BROWSER_START_ATTEMPTS = 5; 	// number of times to try to launch a browser if it does not start 

	public enum Browser { 
        CHROME("chrome", "Chrome", false), 
        FIREFOX("firefox", "Firefox", false), 
        INTERNET_EXPLORER("ie", "Internet Explorer", false), 
        SAFARI("safari", "Safari", false),
        PHANTOMJS("phantomjs", "PhantomJS", false),
        EDGE("edge", "Edge", false),
        
        CHROME_EMULATION_BLACKBERRY_PLAYBOOK("blackberryPlayBook", "BlackBerry PlayBook", true),
        CHROME_EMULATION_BLACKBERRY_Z30("blackberryZ30", "BlackBerry Z30", true),
        CHROME_EMULATION_GALAXY_NOTE2("galaxyNote2", "Galaxy Note II", true),
        CHROME_EMULATION_GALAXY_NOTE3("galaxyNote3", "Galaxy Note 3", true),
        CHROME_EMULATION_GALAXY_S3("galaxys3", "Galaxy S III", true),
        CHROME_EMULATION_GALAXY_S4("galaxys4", "Galaxy S4", true),
        CHROME_EMULATION_KINDLE_FIRE_HDX("kindleFireHdx", "Amazon Kindle Fire HDX", true),
        CHROME_EMULATION_LG_OPTIMUS_L70("lgOptimusL70", "LG Optimus L70", true),
        CHROME_EMULATION_LAPTOP_WITH_HDPI_SCREEN("laptopWithHDPI", "Laptop with HiDPI screen", true),
        CHROME_EMULATION_LAPTOP_WITH_MDPI_SCREEN("laptopWithMDPI", "Laptop with MDPI screen", true),
        CHROME_EMULATION_LAPTOP_WITH_TOUCH("laptopWithTouch", "Laptop with touch", true),
        CHROME_EMULATION_MICROSOFT_LUMIA_550("microsoftLumia550", "Microsoft Lumia 550", true),
        CHROME_EMULATION_MICROSOFT_LUMIA_950("microsoftLumia950", "Microsoft Lumia 950", true),
        CHROME_EMULATION_NEXUS10("nexus10", "Nexus 10", true),
        CHROME_EMULATION_NEXUS4("nexus4", "Nexus 4", true),
        CHROME_EMULATION_NEXUS5("nexus5", "Nexus 5", true),
        CHROME_EMULATION_NEXUS6("nexus6", "Nexus 6", true),
        CHROME_EMULATION_NEXUS7("nexus7", "Nexus 7", true),
        CHROME_EMULATION_NOKIA_LUMIA520("nokiaLumia520", "Nokia Lumia 520", true),
        CHROME_EMULATION_NOKIA_N9("nokiaN9", "Nokia N9", true),
        CHROME_EMULATION_IPAD_MINI("ipadmini", "iPad Mini", true),
        CHROME_EMULATION_IPHONE4("iphone4", "iPhone 4", true),
        CHROME_EMULATION_GALAXY_S5("galaxys5", "Galaxy S5", true),
        CHROME_EMULATION_NEXUS5X("nexus5x", "Nexus 5X", true),       
        CHROME_EMULATION_PIXEL2("pixel2", "Pixel 2", true),
        CHROME_EMULATION_PIXEL2XL("pixel2xl", "Pixel 2 XL", true),
        CHROME_EMULATION_NEXUS6P("nexus6p", "Nexus 6P", true),
        CHROME_EMULATION_IPHONE5("iphone5", "iPhone 5", true),
        CHROME_EMULATION_IPHONE6("iphone6", "iPhone 6", true),
        CHROME_EMULATION_IPHONE6PLUS("iphone6Plus", "iPhone 6 Plus", true),
        CHROME_EMULATION_IPHONE7("iphone7", "iPhone 7", true),
        CHROME_EMULATION_IPHONE7PLUS("iphone7Plus", "iPhone 7 Plus", true),
        CHROME_EMULATION_IPHONE8("iphone8", "iPhone 8", true),
        CHROME_EMULATION_IPHONE8PLUS("iphone8Plus", "iPhone 8 Plus", true),
        CHROME_EMULATION_IPHONEX("iphoneX", "iPhone X", true),
        CHROME_EMULATION_IPAD("ipad", "iPad", true),
        CHROME_EMULATION_IPAD_PRO("ipadPro", "iPad Pro", true),
        ; 

        String driverStringName; 
        String driverDescription;
        boolean isEmulated; 

        Browser(String browser, String description, boolean isEmulated) { 
            this.driverStringName = browser; 
            this.driverDescription = description; 
            this.isEmulated = isEmulated; 
        }

        @Override
        public String toString() {
            return driverStringName; 
        };

        public String getDescription() { 
            return driverDescription; 
        }

        public static Browser fromString(String str) {
            if (str != null) { 
                for (Browser b : Browser.values()) { 
                    if (str.equalsIgnoreCase(b.toString())) { 
                        return b; 
                    }
                }
            }
            throw new IllegalArgumentException("Invalid driver name: " + str); 
        }
	}
	
	public enum OS { 
        WINDOWS,  
        MAC,
        LINUX, 
        IOS,
        Android,
        UNKNOWN; 

        public static OS getOS() { 
            String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (osName.contains("mac") || osName.contains("darwin")) {
                return MAC; 
            } else if (osName.contains("win")) {
                return WINDOWS;
            } else if (osName.contains("nux")) {
                return LINUX;
            } else if (osName.contains("ios")){
            	return IOS;
            } else if(osName.contains("android")) {
            	return Android;
            } else {
            	return UNKNOWN;
            }
        }
    }
	
	public enum MOBILE_DRIVER {
		IOS("ios", "iOS"),
		Android("android", "Android");

		String driverStringName; 
		String driverDescription;

		MOBILE_DRIVER(String browser, String description) { 
			this.driverStringName = browser; 
			this.driverDescription = description; 
		}

		@Override
		public String toString() {
			return driverStringName; 
		};

		public String getDescription() { 
			return driverDescription; 
		}
		

		public static MOBILE_DRIVER fromString(String str) {
			if (str != null) { 
				for (MOBILE_DRIVER d : MOBILE_DRIVER.values()) { 
					if (str.equalsIgnoreCase(d.toString())) { 
						return d; 
					}
				}
			}
			throw new IllegalArgumentException("Invalid mobile driver name: " + str); 
		}

	}

	public enum APP {
		ANDROID("Vida.apk"), IOS("Vida.app");
		private final String appName;

		private APP(final String appName) {
			this.appName = appName;
		}

		public String getAppName() {
			return this.appName;
		}
	}

}
