package com.rasodu.gedcom.Validation;

public class HusbandWifeIdentifier {
	
	public String HusbandId;
	public String WifeId;
	
	
	
	public HusbandWifeIdentifier(String HusbandId, String WifeId) {
		this.HusbandId = HusbandId;
		this.WifeId = WifeId;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		return hashCode() == other.hashCode();
	}
	
	@Override
	public int hashCode() {
		//Create a composite hash code with prime multiplication.
		//For performance, using a Mersenne prime 2^n - 1.
		//Using a relatively large prime number reduces the chance of a collision.
		int result = 1;
		if(HusbandId != null) {
			result = result * 8191 + HusbandId.hashCode();
		}
		if(WifeId != null) {
			result = result * 8191 + WifeId.hashCode();
		}
		return result;
	}
}
