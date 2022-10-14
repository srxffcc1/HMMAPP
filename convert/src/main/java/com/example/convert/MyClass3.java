package com.example.convert;

import java.util.ArrayList;
import java.util.List;

public class MyClass3 {
    public static void main(String[] args) {
//        final Map<String, List<String>> modelMap=new HashMap<>();
//        Util_File.getFiles("D:\\APPSVNJSBD\\HMM-C-Android", ".+(.java|.xml)$", new CallBack() {
//            @Override
//            public void back(Object... object) {
//                File file= (File) object[0];
//                if(file.getAbsolutePath().endsWith(".java")){
//                    if(!file.getAbsolutePath().contains("build\\")){
//
//                    }
//                }else if(file.getAbsolutePath().contains("layout\\")){
//                    String modelname=file.getAbsolutePath().replace("D:\\APPSVNJSBD\\HMM-C-Android\\","").split("\\\\")[0];
//                    //System.out.println(modelname+":"+file.getName());
//                    File parent=file.getParentFile();
//                    String filename=modelname+"_"+file.getName().replace(modelname+"_","");
//                    List<String> result=new ArrayList<>();
//                    if(modelMap.get(modelname)==null){
//                        modelMap.put(modelname,result);
//                    }else {
//                        result=modelMap.get(modelname);
//                    }
//                    result.add(file.getName());
//                    if(!modelname.equals("library")){
//
//                        file.renameTo(new File(parent.getAbsolutePath()+"\\"+filename));
//                    }
//                }
//            }
//        });
//        for (Map.Entry<String, List<String>> entry : modelMap.entrySet()){
//            String key = entry.getKey();
//            List<String> value=entry.getValue();
//            for (String s : value) {
//                //System.out.println(key+":"+s);
//            }
//        }
//        Util_File.getFiles("D:\\APPSVNJSBD\\HMM-C-Android", ".+(.java|.xml)$", new CallBack() {
//            @Override
//            public void back(Object... object) {
//                File file= (File) object[0];
//                if(file.getAbsolutePath().endsWith(".java")){
//                    if(!file.getAbsolutePath().contains("build\\")){
//                        String javacontext=Util_File.inputStream2String(file);
//                        String javacontextFinal=javacontext;
//                        Pattern pattern=Pattern.compile(".*?R\\.layout\\.(.+)?");
//                        String modelname=file.getAbsolutePath().replace("D:\\APPSVNJSBD\\HMM-C-Android\\","").split("\\\\")[0];
//                        Matcher matcher=pattern.matcher(javacontext);
//                        List<String> result=new ArrayList<>();
//                        if(modelMap.get(modelname)==null){
//                            modelMap.put(modelname,result);
//                        }else {
//                            result=modelMap.get(modelname);
//                        }
//                        while (matcher.find()){
//                            //System.out.println(modelname+":"+matcher.group(1));
//                            if(isLayoutInThisModel(result,matcher.group(1))){
//                                javacontextFinal=javacontextFinal.replace(matcher.group(1),modelname+"_"+(matcher.group(1)).replace(modelname+"_",""));
//                            }
//                        }
//                        if(!modelname.equals("library")){
//                            Util_File.string2Stream(javacontextFinal,file);
//                        }
//                    }
//                }else if(file.getAbsolutePath().contains("layout\\")){
//                }
//            }
//
//            private boolean isLayoutInThisModel(List<String> list,String desname) {
//                for (int i = 0; i < list.size(); i++) {
//                    if(desname.contains(list.get(i))){
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        List<String> test=new ArrayList<>();
        test.add("宝宝起名");
        test.add("宝宝起名");
        test.add("宝宝起名");
        test.add("宝宝起i");;
        for (int i = 0; i <test.size() ; i++) {
            if(("宝宝起名".equals(test.get(i))|| "宝宝解名".equals(test.get(i))|| "生日分析".equals(test.get(i)))){
                test.remove(i);
                i--;
            }
        }
        for (int i = 0; i <test.size() ; i++) {
            //System.out.println(test.get(i));
        }

    }
}
