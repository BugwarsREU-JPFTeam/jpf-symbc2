//Copyright (C) 2007 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.

//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.

//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.

package gov.nasa.jpf.symbc.bytecode;


import gov.nasa.jpf.symbc.bytecode.util.IFInstrSymbHelper;
import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

//we should factor out some of the code and put it in a parent class for all "if statements"

public class IF_ICMPNE extends gov.nasa.jpf.jvm.bytecode.IF_ICMPNE{
	public IF_ICMPNE(int targetPosition){
	    super(targetPosition);
	  }
	@Override
	public Instruction execute (ThreadInfo ti) {

		StackFrame sf = ti.getModifiableTopFrame();

		IntegerExpression sym_v1 = (IntegerExpression) sf.getOperandAttr(1);
		IntegerExpression sym_v2 = (IntegerExpression) sf.getOperandAttr(0);

		if ((sym_v1 == null) && (sym_v2 == null)) { // both conditions are concrete
			//System.out.println("Execute IF_ICMPNE: The conditions are concrete");
			return super.execute(ti);
		}else{ // at least one condition is symbolic
			
			Instruction nxtInstr = IFInstrSymbHelper.getNextInstructionAndSetPCChoice(ti, 
																					  this, 
																					  sym_v1,
																					  sym_v2,
																					  Comparator.NE, 
																					  Comparator.EQ);
			if(nxtInstr==getTarget())
				conditionValue=true;
			else 
				conditionValue=false;
			return nxtInstr;
		}
	}
}