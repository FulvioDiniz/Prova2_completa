package prova2.controlechaves.model;

public class Chave {

    private Long codigo;
    private String sala;
    private boolean disponivel;
    private Situacao situacao = Situacao.ATIVO;

	public Chave() {
    }

    public Chave(Long codigo, String sala) {
        this.codigo = codigo;
        this.sala = sala;
        disponivel = true;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void retirar() {
        disponivel = false;
    }

    public void devolver() {
        disponivel = true;
    }

    public void setDisponivel(boolean disponivel) {
    	this.disponivel = disponivel;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
    
    @Override
    public String toString() {
        return sala;
    }

    
}
