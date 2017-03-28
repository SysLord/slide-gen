package de.syslord.boxmodel.renderer.html;

public class AttributedStringInfo {

	private String string;

	private boolean bold;

	private boolean italic;

	public AttributedStringInfo(String string, boolean bold, boolean italic) {
		this.string = string;
		this.bold = bold;
		this.italic = italic;
	}

	public String getString() {
		return string;
	}

	public int getLength() {
		return string.length();
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	@Override
	public String toString() {
		return "AttributedStringInfo [string=" + string + ", bold=" + bold + ", italic=" + italic + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bold ? 1231 : 1237);
		result = prime * result + (italic ? 1231 : 1237);
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AttributedStringInfo other = (AttributedStringInfo) obj;
		if (bold != other.bold) {
			return false;
		}
		if (italic != other.italic) {
			return false;
		}
		if (string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!string.equals(other.string)) {
			return false;
		}
		return true;
	}

}
