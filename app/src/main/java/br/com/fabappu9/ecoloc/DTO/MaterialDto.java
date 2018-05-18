package br.com.fabappu9.ecoloc.DTO;

public class MaterialDto {
    private String id;
    private String descricao;
    private boolean Marcado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isMarcado() {
        return Marcado;
    }

    public void setMarcado(boolean marcado) {
        Marcado = marcado;
    }
}

