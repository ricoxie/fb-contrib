/*
 * fb-contrib - Auxiliary detectors for Java programs
 * Copyright (C) 2005-2014 Dave Brosius
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.mebigfatguy.fbcontrib.detect;

import org.apache.bcel.classfile.Code;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.OpcodeStack.CustomUserValue;
import edu.umd.cs.findbugs.ba.ClassContext;

@CustomUserValue
public class ModifyingUnmodifiableCollection extends BytecodeScanningDetector {

    private BugReporter bugReporter;
    private OpcodeStack stack;
    
    public ModifyingUnmodifiableCollection(BugReporter reporter) {
        bugReporter = reporter;
    }
    
    @Override
    public void visitClassContext(ClassContext context) {
        try {
            stack = new OpcodeStack();
            super.visitClassContext(context);
        } finally {
            stack = null;
        }
    }
    
    @Override
    public void visitCode(Code obj) {
        stack.resetForMethodEntry(this);
    }
    
    public void sawOpcode(int seen) {
        try {
            stack.precomputation(this);
        } finally {
            stack.sawOpcode(this, seen);
        }
    }
}