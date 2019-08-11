package VidaAutomation.VidaAutomation.utilities;

public class Console {
    public static enum Color { 
        RESET("\u001B[0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),

    	 // Bold
        BLACK_BOLD("\033[1;30m"), 
        RED_BOLD("\033[1;31m"),     
        GREEN_BOLD("\033[1;32m"),   
        YELLOW_BOLD("\033[1;33m"),  
        BLUE_BOLD("\033[1;34m"),   
        MAGENTA_BOLD("\033[1;35m"), 
        CYAN_BOLD("\033[1;36m"),    
        WHITE_BOLD("\033[1;37m"),   

        // Underline
        BLACK_UNDERLINED("\033[4;30m"),     
        RED_UNDERLINED("\033[4;31m"),       
        GREEN_UNDERLINED("\033[4;32m"),     
        YELLOW_UNDERLINED("\033[4;33m"),    
        BLUE_UNDERLINED("\033[4;34m"),      
        MAGENTA_UNDERLINED("\033[4;35m"),   
        CYAN_UNDERLINED("\033[4;36m"),      
        WHITE_UNDERLINED("\033[4;37m"),     

        // Background
        BLACK_BACKGROUND("\033[40m"),   
        RED_BACKGROUND("\033[41m"),     
        GREEN_BACKGROUND("\033[42m"),  
        YELLOW_BACKGROUND("\033[43m"), 
        BLUE_BACKGROUND("\033[44m"),    
        MAGENTA_BACKGROUND("\033[45m"), 
        CYAN_BACKGROUND("\033[46m"),    
        WHITE_BACKGROUND("\033[47m"),   

        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),     
        RED_BRIGHT("\033[0;91m"),       
        GREEN_BRIGHT("\033[0;92m"),     
        YELLOW_BRIGHT("\033[0;93m"),   
        BLUE_BRIGHT("\033[0;94m"),      
        MAGENTA_BRIGHT("\033[0;95m"),   
        CYAN_BRIGHT("\033[0;96m"),      
        WHITE_BRIGHT("\033[0;97m"),    

        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),    
        RED_BOLD_BRIGHT("\033[1;91m"),      
        GREEN_BOLD_BRIGHT("\033[1;92m"),    
        YELLOW_BOLD_BRIGHT("\033[1;93m"),   
        BLUE_BOLD_BRIGHT("\033[1;94m"),     
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),  
        CYAN_BOLD_BRIGHT("\033[1;96m"),     
        WHITE_BOLD_BRIGHT("\033[1;97m"),    

        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     
        RED_BACKGROUND_BRIGHT("\033[0;101m"),       
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");    
    	private String strVal; 

        Color(String str) { 
            strVal = str; 
        }

        @Override public String toString() { 
            return strVal; 
        }
    }

    /** 
     * Output to the console in Red if supported
     * @param msg
     */
    public static void printRed(String msg) { 
        printColor(msg, Color.RED); 
    }

    /** 
     * Output to the console in Green if supported
     * @param msg
     */
    public static void printGreen(String msg) { 
        printColor(msg, Color.GREEN); 
    }

    public static void printCyan(String msg) { 
        printColor(msg, Color.CYAN); 
    }

    public static void printBlue(String msg) { 
        printColor(msg, Color.BLUE);
    }

    public static void print(String msg) {
        System.out.println(msg);	
    }

    public static void printColor(String msg, Color color) { 
        System.out.println(color + msg + Color.RESET);		
    }

    public static void printColorSameLine(String msg, Color color) { 
        System.out.print(color + msg + Color.RESET);		
    }
    
}

