package br.com.fabappu9.ecoloc.Model;


public class RespostaLogin {

    private String RETORNO;
    private String ID;
    private String NOME;
    private String LOGIN;
    private String FOTO;
    private String SENHA;

    public String getFOTO() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
    }

    public String getSENHA() {
        return SENHA;
    }

    public void setSENHA(String SENHA) {
        this.SENHA = SENHA;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public void setLOGIN(String LOGIN) {
        this.LOGIN = LOGIN;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRETORNO() {
        return RETORNO;
    }

    public void setRETORNO(String RETORNO) {
        this.RETORNO = RETORNO;
    }


}
