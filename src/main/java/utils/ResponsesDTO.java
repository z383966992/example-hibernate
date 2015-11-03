package utils;

import java.io.Serializable;

public class ResponsesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code = ReturnCode.ACTIVE_FAILURE.code();
	private String msg = ReturnCode.ACTIVE_FAILURE.msg();
	private Object attach;

	public ResponsesDTO(ReturnCode returnCode) {
		this.code = returnCode.code();
		this.msg = returnCode.msg();
	}

	public void setReturnCode(ReturnCode returnCode) {
		this.code = returnCode.code();
		this.msg = returnCode.msg();
	}

	public ReturnCode nowReturnCode() {// 此处不能写getxx,会被spring 识别然后返回出去
		return ReturnCode.codeToEnum(code);
	}

	public ResponsesDTO(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@SuppressWarnings("unchecked")
	public <E> E getAttach() {
		return (E) attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
