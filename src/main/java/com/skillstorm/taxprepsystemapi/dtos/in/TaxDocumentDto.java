package com.skillstorm.taxprepsystemapi.dtos.in;

import com.skillstorm.taxprepsystemapi.enums.MaritalStatus;
import com.skillstorm.taxprepsystemapi.models.Form1099;
import com.skillstorm.taxprepsystemapi.models.FormW2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDocumentDto {

    String userId;
    MaritalStatus maritalStatus;
    List<FormW2> formW2s;
    List<Form1099> form1099s;

}
