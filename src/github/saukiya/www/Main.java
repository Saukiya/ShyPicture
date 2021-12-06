package github.saukiya.www;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_");
        SimpleDateFormat sdfNormal = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        List<String> whiteList = Arrays.asList("out", "src", ".idea", ".git", ".gitignore", "ShyPicture.iml", "ClassifiedPicture.jar", ".START.bat");
        File parentFile = new File("./");
        List<String> lockList = new ArrayList<>(Arrays.stream(parentFile.listFiles()).filter(file -> file.getName().startsWith("IMG_")).map(file -> file.getName().substring(4,file.getName().lastIndexOf("."))).toList());
        List<File> fileList = Arrays.stream(parentFile.listFiles())
                .filter(file -> !whiteList.contains(file.getName()) && !file.getName().startsWith("IMG_"))
                .sorted(new FileComparator()).toList();
        for (File file : fileList) {
            String fileName = file.getName();
            String prefix = sdf.format(new Date(file.lastModified()));
            int index = 1;
            String suffix = fileName.substring(fileName.length() - 4);
            while (lockList.contains(prefix+index)) {
                index++;
            }
            lockList.add(prefix+index);
            System.out.println("RENAME: " + prefix + index + suffix + '\t' + sdfNormal.format(file.lastModified()) + '\t' + file.length());
            file.renameTo(new File(parentFile, "IMG_" + prefix + index + suffix));
        }
    }

    public static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            int d1 = Boolean.compare(file1.isDirectory(), file2.isDirectory());
            int d2 = Long.compare(file1.lastModified(), file2.lastModified());
            int d3 = Long.compare(file1.length(), file2.length());
            if (d1 != 0) return d1;
            if (d2 != 0) return d2;
            return d3;
        }
    }
}
