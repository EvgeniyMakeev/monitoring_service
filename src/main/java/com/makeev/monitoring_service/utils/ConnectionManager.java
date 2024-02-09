package com.makeev.monitoring_service.utils;

import java.sql.Connection;

public interface ConnectionManager {
    Connection open();
}
