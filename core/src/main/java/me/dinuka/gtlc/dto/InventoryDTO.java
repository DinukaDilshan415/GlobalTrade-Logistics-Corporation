package me.dinuka.gtlc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private String product_name;
    private String hs_code;
    private String quantity;
    private String unit_value;
    private String warehouses_id;
}
