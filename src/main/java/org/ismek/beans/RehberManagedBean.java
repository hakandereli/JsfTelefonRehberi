package org.ismek.beans;

import org.ismek.db.DbOperations;
import org.ismek.domain.Rehber;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Facelet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "rehberMB")
public class RehberManagedBean {

    private int id;
    private String isim;
    private String telefon;
    private List<Rehber> rehberList;
    private List<Rehber> filteredRehberlist;

    public RehberManagedBean() {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String rehberId = req.getParameter("rehberId");
        if (rehberId != null) {
            Rehber rehber = DbOperations.kisiGetir(Integer.valueOf(rehberId));
            id = rehber.getId();
            isim = rehber.getIsim();
            telefon = rehber.getTelefon();
        }
    }

    @PostConstruct
    public void postConstruct() {
        rehberList = DbOperations.tumRehberiGetir();
    }

    public void save() {
        DbOperations.rehbereEkle(isim, telefon);
        sendRedirect("./index.xhtml");
    }

    public void edit(int id) {
        sendRedirect("./edit.xhtml?rehberId=" + id);
    }

    public void fetchDeleteRow(int id) {
        sendRedirect("./delete.xhtml?rehberId=" + id);
    }

    public void delete(){
        DbOperations.kisiSil(id);
        addMessage("islem basarili", "Kayit Silindi");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendRedirect("./index.xhtml");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void sendRedirect(String url) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        ExternalContext externalContext = currentInstance.getExternalContext();
        try {
            externalContext.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        DbOperations.kisiGuncelle(id, isim, telefon);
        sendRedirect("./index.xhtml");
    }

    public void ara() {
        List<Rehber> rehberListesi = DbOperations.kisiGetir(isim);
        for (Rehber rehber : rehberListesi) {
            System.out.println(rehber);
        }
    }

    public List<Rehber> getRehberList() {
        return rehberList;
    }

    public void setRehberList(List<Rehber> rehberList) {
        this.rehberList = rehberList;
    }

    public List<Rehber> getFilteredRehberlist() {
        return filteredRehberlist;
    }

    public void setFilteredRehberlist(List<Rehber> filteredRehberlist) {
        this.filteredRehberlist = filteredRehberlist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}