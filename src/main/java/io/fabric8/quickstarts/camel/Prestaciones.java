/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package io.fabric8.quickstarts.camel;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Prestacion Object")
public class Prestaciones {

	@ApiModelProperty(notes = "ID de la prestacion", required = false, example = "123")
	private int id;

	@ApiModelProperty(notes = "Nombre del Paciente", required = true, example = "Prestacionaldo")
	private String patient;

	@ApiModelProperty(notes = "Examen", required = false, example = "Examen X")
	private String exam;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Santiago")
	@ApiModelProperty(notes = "Fecha de ingreso de la prestacion en formato yyyy-MM-dd", required = false, example = "1898-02-12")
	private Date dateIn;

	@ApiModelProperty(notes = "Cantidad maxima de dias para informar", required = false, example = "5")
	private int maxDaysToBeforeInform;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Santiago")
	@ApiModelProperty(notes = "Fecha de informe real en formato yyyy-MM-dd", required = false, example = "1998-02-12")
	private Date informDate;

	@ApiModelProperty(notes = "Base64 de la imagen", required = false, example = "qwerty")
	private String examImageId;

	@ApiModelProperty(notes = "ID Tipo de la prestacion", required = false, example = "99")
	private int type;

	@ApiModelProperty(notes = "Registro medico", required = false, example = "Esto es un registro medico")
	private String medicalRecord;

	public Prestaciones() {
	}

	public Prestaciones(int id, String patient, String exam, Date dateIn, int maxDaysToBeforeInform, Date informDate,
			String examImageId, int type, String medicalRecord) {
		this.id = id;
		this.patient = patient;
		this.exam = exam;
		this.dateIn = dateIn;
		this.maxDaysToBeforeInform = maxDaysToBeforeInform;
		this.informDate = informDate;
		this.examImageId = examImageId;
		this.type = type;
		this.medicalRecord = medicalRecord;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}

	public int getMaxDaysToBeforeInform() {
		return maxDaysToBeforeInform;
	}

	public void setMaxDaysToBeforeInform(int maxDaysToBeforeInform) {
		this.maxDaysToBeforeInform = maxDaysToBeforeInform;
	}

	public Date getInformDate() {
		return informDate;
	}

	public void setInformDate(Date informDate) {
		this.informDate = informDate;
	}

	public String getExamImageId() {
		return examImageId;
	}

	public void setExamImageId(String examImageId) {
		this.examImageId = examImageId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(String medicalRecord) {
		this.medicalRecord = medicalRecord;
	}

	@Override
	public String toString() {
		return "{ id: " + getId() + ", patient: " + getPatient() + ", exam: " + getExam() + ", dateIn: "
				+ (getDateIn() != null ? getDateIn().toString() : "") + ", maxDaysToBeforeInform: "
				+ getMaxDaysToBeforeInform() + ", informDate: "
				+ (getDateIn() != null ? getInformDate().toString() : "") + ", examImageId: " + getExamImageId()
				+ ", type: " + getType() + ", medicalRecord: " + getMedicalRecord() + "}";
	}
}
