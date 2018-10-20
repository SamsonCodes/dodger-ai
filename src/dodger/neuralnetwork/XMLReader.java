/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.neuralnetwork;

import java.util.ArrayList;

public class XMLReader 
{
    public static String getElement(String source, String header)
    {
//        System.out.println("XMLReader: getElement(source = " + source 
//                + ", header = " + header + ")");
        String element = "";
        String currentIdentifier = "";
        boolean identifierStart = false;
        boolean headerFound = false;
        boolean endFound = false;
        for(int i = 0; i < source.length(); i++)
        {
//            System.out.println("Iteration " + i + "/" + (source.length() - 1));
//            System.out.println("endFound = " + endFound);
//            System.out.println("headerFound = " + headerFound);
//            System.out.println("identifierStart = " + identifierStart);
//            System.out.println("currentIdentifier = " + currentIdentifier);
//            System.out.println("element = " + element);
            if(!endFound)
            {
                if(!headerFound)
                {
                    if(!identifierStart)
                    {
                        if(source.charAt(i) == '<')
                        {
                            identifierStart = true;
                        }
                    }
                    else
                    {
                        if(source.charAt(i) == '>')
                        {
                            identifierStart = false;
                            if(currentIdentifier.equals(header))
                            {
                                headerFound = true;
                            }
                            currentIdentifier = "";
                        }
                        else
                        {
                            currentIdentifier += source.charAt(i);
                        }
                    }
                }
                else
                {
                    element+=source.charAt(i);
                    if(!identifierStart)
                    {
                        if(source.charAt(i) == '<')
                        {
                            identifierStart = true;
                        }
                    }
                    else
                    {
                        if(source.charAt(i) == '>')
                        {
                            identifierStart = false;
                            if(currentIdentifier.equals("/" + header))
                            {
                                //System.out.println("XMLReader: endFound!");
                                endFound = true;
                                element = element.replace("</" + header + ">", "");
                            }
                            currentIdentifier = "";
                        }
                        else
                        {
                            currentIdentifier += source.charAt(i);
                        }
                    }

                }
            }
            else
            {
                break;
            }
        }
//        if(element.equals(""))
//        {
//            System.out.println("XMLReader: getElement(" + source + "," + header 
//                    + "): no element found!");
//        }
        return element;
    }
    
    public static String getElement(ArrayList<String> source, String header)
    {
//        System.out.println("XMLReader: getElement(source = " + source 
//                + ", header = " + header + ")");
        String element = "";
        String currentIdentifier = "";
        boolean identifierStart = false;
        boolean headerFound = false;
        boolean endFound = false;
        for(int i = 0; i < source.size(); i++)
        {
            for(int j = 0; j < source.get(i).length(); j++)
            {
    //            System.out.println("Iteration " + i + "/" + (source.length() - 1));
    //            System.out.println("endFound = " + endFound);
    //            System.out.println("headerFound = " + headerFound);
    //            System.out.println("identifierStart = " + identifierStart);
    //            System.out.println("currentIdentifier = " + currentIdentifier);
    //            System.out.println("element = " + element);
                if(!endFound)
                {
                    if(!headerFound)
                    {
                        if(!identifierStart)
                        {
                            if(source.get(i).charAt(j) == '<')
                            {
                                identifierStart = true;
                            }
                        }
                        else
                        {
                            if(source.get(i).charAt(j) == '>')
                            {
                                identifierStart = false;
                                if(currentIdentifier.equals(header))
                                {
                                    headerFound = true;
                                }
                                currentIdentifier = "";
                            }
                            else
                            {
                                currentIdentifier += source.get(i).charAt(j);
                            }
                        }
                    }
                    else
                    {
                        element+=source.get(i).charAt(j);
                        if(!identifierStart)
                        {
                            if(source.get(i).charAt(j) == '<')
                            {
                                identifierStart = true;
                            }
                        }
                        else
                        {
                            if(source.get(i).charAt(j) == '>')
                            {
                                identifierStart = false;
                                if(currentIdentifier.equals("/" + header))
                                {
                                    //System.out.println("XMLReader: endFound!");
                                    endFound = true;
                                    element = element.replace("</" + header + ">", "");
                                }
                                currentIdentifier = "";
                            }
                            else
                            {
                                currentIdentifier += source.get(i).charAt(j);
                            }
                        }

                    }
                }
                else
                {
                    break;
                }
            }
        }
//        if(element.equals(""))
//        {
//            System.out.println("XMLReader: getElement(" + source + "," + header 
//                    + "): no element found!");
//        }
        return element;
    }
    
    public static ArrayList<String> getElements(String source, String header)
    {
//        System.out.println("XMLReader: getElement(source = " + source 
//                + ", header = " + header + ")");
        ArrayList<String> elements = new ArrayList<>();
        String element = "";
        String currentIdentifier = "";
        boolean identifierStart = false;
        boolean headerFound = false;
        for(int i = 0; i < source.length(); i++)
        {
//            System.out.println("Iteration " + i + "/" + (source.length() - 1));
//            System.out.println("endFound = " + endFound);
//            System.out.println("headerFound = " + headerFound);
//            System.out.println("identifierStart = " + identifierStart);
//            System.out.println("currentIdentifier = " + currentIdentifier);
//            System.out.println("element = " + element);
            if(!headerFound)
            {
                if(!identifierStart)
                {
                    if(source.charAt(i) == '<')
                    {
                        identifierStart = true;
                    }
                }
                else
                {
                    if(source.charAt(i) == '>')
                    {
                        identifierStart = false;
                        if(currentIdentifier.equals(header))
                        {
                            headerFound = true;
                        }
                        currentIdentifier = "";
                    }
                    else
                    {
                        currentIdentifier += source.charAt(i);
                    }
                }
            }
            else
            {
                element+=source.charAt(i);
                if(!identifierStart)
                {
                    if(source.charAt(i) == '<')
                    {
                        identifierStart = true;
                    }
                }
                else
                {
                    if(source.charAt(i) == '>')
                    {
                        identifierStart = false;
                        if(currentIdentifier.equals("/" + header))
                        {
                            element = element.replace("</" + header + ">", "");
                            elements.add(element);
                            element = "";
                        }
                        currentIdentifier = "";
                    }
                    else
                    {
                        currentIdentifier += source.charAt(i);
                    }
                }

            }
        }
//        if(elements.isEmpty())
//        {
//            System.out.println("XMLReader: getElements(" + source + "," + header 
//                    + "): no elements found!");
//        }
        return elements;
    }
    
    public static ArrayList<String> getElements(ArrayList<String> source, String header)
    {
//        System.out.println("XMLReader: getElement(source = " + source 
//                + ", header = " + header + ")");
        ArrayList<String> elements = new ArrayList<>();
        String element = "";
        String currentIdentifier = "";
        boolean identifierStart = false;
        boolean headerFound = false;
        for(int i = 0; i < source.size(); i++)
        {
            for(int j = 0; j < source.get(i).length(); j ++)
            {
    //            System.out.println("Iteration " + i + "/" + (source.length() - 1));
    //            System.out.println("endFound = " + endFound);
    //            System.out.println("headerFound = " + headerFound);
    //            System.out.println("identifierStart = " + identifierStart);
    //            System.out.println("currentIdentifier = " + currentIdentifier);
    //            System.out.println("element = " + element);
                if(!headerFound)
                {
                    if(!identifierStart)
                    {
                        if(source.get(i).charAt(j) == '<')
                        {
                            identifierStart = true;
                        }
                    }
                    else
                    {
                        if(source.get(i).charAt(j) == '>')
                        {
                            identifierStart = false;
                            if(currentIdentifier.equals(header))
                            {
                                headerFound = true;
                            }
                            currentIdentifier = "";
                        }
                        else
                        {
                            currentIdentifier += source.get(i).charAt(j);
                        }
                    }
                }
                else
                {
                    element+=source.get(i).charAt(j);
                    if(!identifierStart)
                    {
                        if(source.get(i).charAt(j) == '<')
                        {
                            identifierStart = true;
                        }
                    }
                    else
                    {
                        if(source.get(i).charAt(j) == '>')
                        {
                            identifierStart = false;
                            if(currentIdentifier.equals("/" + header))
                            {
                                element = element.replace("</" + header + ">", "");
                                elements.add(element);
                                element = "";
                                headerFound = false;
                            }
                            currentIdentifier = "";
                        }
                        else
                        {
                            currentIdentifier += source.get(i).charAt(j);
                        }
                    }
                }
            }
        }
//        if(elements.isEmpty())
//        {
//            System.out.println("XMLReader: getElements(" + source + "," + header 
//                    + "): no elements found!");
//        }
        return elements;
    }
}
