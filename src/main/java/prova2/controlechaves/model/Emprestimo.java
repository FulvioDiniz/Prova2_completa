package prova2.controlechaves.model;

import java.time.LocalDateTime;

public class Emprestimo {
	
    private Long codigo;
    private LocalDateTime dataRetirada;
    private LocalDateTime dataDevolucao;
    private Servidor servidorRetirou;
    private Servidor servidorDevolveu;
    private Chave chave;
    private Situacao situacao = Situacao.ATIVO;

    public Emprestimo() {
    }

    

    public Emprestimo(Long codigo, LocalDateTime dataRetirada, LocalDateTime dataDevolucao, Servidor servidorRetirou,
            Servidor servidorDevolveu, Chave chave, Situacao situacao) {
        this.codigo = codigo;
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
        this.servidorRetirou = servidorRetirou;
        this.servidorDevolveu = servidorDevolveu;
        this.chave = chave;
        this.situacao = situacao;
    }



    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDateTime dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Servidor getServidorRetirou() {
        return servidorRetirou;
    }

    public void setServidorRetirou(Servidor servidorRetirou) {
        this.servidorRetirou = servidorRetirou;
    }

    public Servidor getServidorDevolveu() {
        return servidorDevolveu;
    }

    public void setServidorDevolveu(Servidor servidorDevolveu) {
        this.servidorDevolveu = servidorDevolveu;
    }

    public Chave getChave() {
        return chave;
    }

    public void setChave(Chave chave) {
        this.chave = chave;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\nRetirada: " + getDataRetirada() + "\nServidor: " + servidorRetirou + "\nDevolucao: " + getDataDevolucao() + "\nServidor: " + servidorDevolveu + '\n';
    }
    
}
