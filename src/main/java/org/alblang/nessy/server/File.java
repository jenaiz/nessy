package org.alblang.nessy.server;

import java.util.Scanner;

/**
 * @author jesus.navarrete  (02/10/15)
 */
public class File {

    public static void main(String[] args) {
        File f = new File();
        f.p();
        f.c();
    }

    public void p() {
//        System.out.println("---- " + File.class.getClassLoader().getResource(".").toString());
//        System.out.println("---- " + Thread.currentThread().getContextClassLoader().getResource(".").toString());

//        System.out.println("---- " + this.getClass().getClassLoader().getResource("."));
//        System.out.println("---- " + this.getClass().getResource("."));
        System.out.println("---- " + this.getClass().getResourceAsStream("../../../../WEB-INF/web.xml"));
    }

    public void c() {
        final Scanner sc = new Scanner(this.getClass().getResourceAsStream("../../../../WEB-INF/web.xml"));
        while (sc.hasNext()) {
            System.out.println(sc.next());
        }
    }

}
