package com.company.inventory.desktop.sync;

import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.model.ProductDto;

public class ConflictResolver {
    
    public static LocalProductDto resolve(LocalProductDto local, ProductDto server) {
        if (server != null && local != null && server.getVersion() != null && local.getVersion() != null) {
            
            if (server.getVersion() > local.getVersion()) {
                local.setName(server.getName());
                local.setPrice(server.getPrice());
                local.setStockQuantity(server.getStockQuantity());
                local.setVersion(server.getVersion());
                
                local.setSyncStatus("SYNCED"); 
            }
        }
        return local;
    }
}