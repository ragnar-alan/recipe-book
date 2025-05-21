package com.ragnaralan.recipebook;

import org.approvaltests.core.ApprovalFailureReporter;
import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.JunitReporter;

public class PackageSettings {
    public static ApprovalFailureReporter FrontloadedReporter = new JunitReporter();
    public static ApprovalFailureReporter UseReporter = new ClipboardReporter();
    public static String UseApprovalSubdirectory = "approvals";
    public static String ApprovalBaseDirectory = "../resources";
}
