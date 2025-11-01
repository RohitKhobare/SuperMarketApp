import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class SupermarketManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/supermarket_db";
    private static final String USER = "superuser"; 
    private static final String PASSWORD = "superpass";
    private Connection connection;
    private JFrame frame;
    private JTable productsTable;

    // UI Constants
    private final Color PRIMARY_COLOR = new Color(40, 167, 69);
    private final Color SECONDARY_COLOR = new Color(33, 37, 41);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public SupermarketManagement() {
        initializeDB();
        createGUI();
    }

    private void initializeDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            showError("Database Connection Failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private void createGUI() {
        frame = new JFrame("Supermarket Management System");
        frame.setSize(1400, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        
        tabbedPane.addTab("Add Product", createAddProductPanel());
        tabbedPane.addTab("Update Product", createUpdateProductTabbedPanel());
        tabbedPane.addTab("View Products", createViewProductsPanel());
        tabbedPane.addTab("Products Expiry View", createProductsExpiryViewPanel());
        tabbedPane.addTab("Alerts", createAlertsPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        JTextField priceField = createStyledTextField();
        JTextField stockField = createStyledTextField();
        JTextField expiryField = createStyledTextField();

        addFormRow(panel, gbc, 0, "Product ID*:", idField);
        addFormRow(panel, gbc, 1, "Product Name*:", nameField);
        addFormRow(panel, gbc, 2, "Price* ($):", priceField);
        addFormRow(panel, gbc, 3, "Stock*:", stockField);
        addFormRow(panel, gbc, 4, "Expiry Date (yyyy-mm-dd):", expiryField);

        JButton addButton = styledButton("Add Product");
        addButton.addActionListener(e -> {
            handleAddProduct(idField, nameField, priceField, stockField, expiryField);
            refreshProductsTable();
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createUpdateProductTabbedPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTabbedPane innerTabbedPane = new JTabbedPane();
        innerTabbedPane.setFont(LABEL_FONT);

        innerTabbedPane.addTab("Product Details", createUpdateDetailsPanel());
        innerTabbedPane.addTab("Add Stock", createAddStockPanel());
        innerTabbedPane.addTab("Sell Product", createSellProductPanel());
        innerTabbedPane.addTab("Delete Product", createDeleteProductPanel());

        panel.add(innerTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUpdateDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        JTextField priceField = createStyledTextField();
        JTextField stockField = createStyledTextField();
        JTextField expiryField = createStyledTextField();

        addFormRow(panel, gbc, 0, "Product ID*:", idField);

        JButton fetchButton = styledButton("Fetch Details");
        fetchButton.addActionListener(e -> handleFetchProduct(
            idField, nameField, priceField, stockField, expiryField
        ));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 10);
        panel.add(fetchButton, gbc);

        addFormRow(panel, gbc, 1, "Name:", nameField);
        addFormRow(panel, gbc, 2, "Price:", priceField);
        addFormRow(panel, gbc, 3, "Stock:", stockField);
        addFormRow(panel, gbc, 4, "Expiry Date:", expiryField);

        JButton updateButton = styledButton("Update Product");
        updateButton.addActionListener(e -> {
            handleUpdateProduct(idField, nameField, priceField, stockField, expiryField);
            refreshProductsTable();
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);

        return panel;
    }

    private JPanel createAddStockPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        nameField.setEditable(false);
        JTextField currentStockField = createStyledTextField();
        currentStockField.setEditable(false);
        JTextField addStockField = createStyledTextField();
        JTextField newStockField = createStyledTextField();
        newStockField.setEditable(false);

        addFormRow(panel, gbc, 0, "Product ID*:", idField);

        JButton fetchButton = styledButton("Fetch Product");
        fetchButton.addActionListener(e -> {
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT product_name, stock FROM products WHERE product_id = ?");
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("product_name"));
                    currentStockField.setText(String.valueOf(rs.getInt("stock")));
                } else {
                    showError("Product not found!");
                }
            } catch (Exception ex) {
                showError("Fetch Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 10);
        panel.add(fetchButton, gbc);

        addFormRow(panel, gbc, 1, "Product Name:", nameField);
        addFormRow(panel, gbc, 2, "Current Stock:", currentStockField);
        addFormRow(panel, gbc, 3, "Add Stock*:", addStockField);
        addFormRow(panel, gbc, 4, "New Stock:", newStockField);

        JButton updateButton = styledButton("Update Stock");
        updateButton.addActionListener(e -> {
            try {
                if (!validateFields(idField, addStockField)) return;

                int addStock = Integer.parseInt(addStockField.getText());
                if (addStock <= 0) {
                    showError("Stock to add must be positive!");
                    return;
                }

                PreparedStatement ps = connection.prepareStatement(
                    "UPDATE products SET stock = stock + ? WHERE product_id = ?");
                ps.setInt(1, addStock);
                ps.setInt(2, Integer.parseInt(idField.getText()));
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    showSuccess("Stock updated successfully!");
                    currentStockField.setText(String.valueOf(
                        Integer.parseInt(currentStockField.getText()) + addStock));
                    newStockField.setText(currentStockField.getText());
                    refreshProductsTable();
                } else {
                    showError("Product not found!");
                }
            } catch (Exception ex) {
                showError("Update Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);

        return panel;
    }

    private JPanel createSellProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        nameField.setEditable(false);
        JTextField currentStockField = createStyledTextField();
        currentStockField.setEditable(false);
        JTextField sellQtyField = createStyledTextField();
        JTextField newStockField = createStyledTextField();
        newStockField.setEditable(false);

        addFormRow(panel, gbc, 0, "Product ID*:", idField);

        JButton fetchButton = styledButton("Fetch Product");
        fetchButton.addActionListener(e -> {
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT product_name, stock FROM products WHERE product_id = ?");
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("product_name"));
                    currentStockField.setText(String.valueOf(rs.getInt("stock")));
                } else {
                    showError("Product not found!");
                }
            } catch (Exception ex) {
                showError("Fetch Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 10);
        panel.add(fetchButton, gbc);

        addFormRow(panel, gbc, 1, "Product Name:", nameField);
        addFormRow(panel, gbc, 2, "Current Stock:", currentStockField);
        addFormRow(panel, gbc, 3, "Sell Quantity*:", sellQtyField);
        addFormRow(panel, gbc, 4, "New Stock:", newStockField);

        JButton sellButton = styledButton("Sell Product");
        sellButton.addActionListener(e -> {
            try {
                if (!validateFields(idField, sellQtyField)) return;

                int sellQty = Integer.parseInt(sellQtyField.getText());
                int currentStock = Integer.parseInt(currentStockField.getText());

                if (sellQty <= 0) {
                    showError("Quantity must be positive!");
                    return;
                }

                if (sellQty > currentStock) {
                    showError("Insufficient stock! Available: " + currentStock);
                    return;
                }

                connection.setAutoCommit(false);

                // Update stock
                PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE products SET stock = stock - ? WHERE product_id = ?");
                updateStmt.setInt(1, sellQty);
                updateStmt.setInt(2, Integer.parseInt(idField.getText()));
                updateStmt.executeUpdate();

                // Record sale
                PreparedStatement saleStmt = connection.prepareStatement(
                    "INSERT INTO sales (product_id, quantity) VALUES (?, ?)");
                saleStmt.setInt(1, Integer.parseInt(idField.getText()));
                saleStmt.setInt(2, sellQty);
                saleStmt.executeUpdate();

                connection.commit();

                showSuccess("Sale processed successfully!");
                newStockField.setText(String.valueOf(currentStock - sellQty));
                currentStockField.setText(newStockField.getText());
                refreshProductsTable();
            } catch (Exception ex) {
                try { connection.rollback(); } catch (SQLException ignored) {}
                showError("Sale Error: " + ex.getMessage());
            } finally {
                try { connection.setAutoCommit(true); } catch (SQLException ignored) {}
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(sellButton, gbc);

        return panel;
    }

    private JPanel createDeleteProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        nameField.setEditable(false);

        addFormRow(panel, gbc, 0, "Product ID*:", idField);

        JButton fetchButton = styledButton("Fetch Product");
        fetchButton.addActionListener(e -> {
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT product_name FROM products WHERE product_id = ?");
                ps.setInt(1, Integer.parseInt(idField.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("product_name"));
                } else {
                    showError("Product not found!");
                }
            } catch (Exception ex) {
                showError("Fetch Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 10, 10);
        panel.add(fetchButton, gbc);

        addFormRow(panel, gbc, 1, "Product Name:", nameField);

        JButton deleteButton = styledButton("Delete Product");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this product?\nThis will also delete all sales records for this product.", 
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (!validateFields(idField)) return;

                    PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM products WHERE product_id = ?");
                    ps.setInt(1, Integer.parseInt(idField.getText()));
                    int rows = ps.executeUpdate();
                    
                    if (rows > 0) {
                        showSuccess("Product deleted successfully!");
                        idField.setText("");
                        nameField.setText("");
                        refreshProductsTable();
                    } else {
                        showError("Product not found!");
                    }
                } catch (Exception ex) {
                    showError("Delete Error: " + ex.getMessage());
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(deleteButton, gbc);

        return panel;
    }

    private JPanel createViewProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        productsTable = new JTable();
        productsTable.setRowHeight(30);
        productsTable.setFont(LABEL_FONT);
        productsTable.setAutoCreateRowSorter(true);
        productsTable.setDefaultRenderer(java.sql.Date.class, new DateCellRenderer());

        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton refreshButton = styledButton("Refresh List");
        refreshButton.addActionListener(e -> refreshProductsTable());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshProductsTable();
        return panel;
    }

    private JPanel createProductsExpiryViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTabbedPane innerTabbedPane = new JTabbedPane();
        innerTabbedPane.setFont(LABEL_FONT);

        // Products with expiry
        JPanel withExpiryPanel = new JPanel(new BorderLayout());
        JTable withExpiryTable = new JTable();
        withExpiryTable.setRowHeight(30);
        withExpiryTable.setFont(LABEL_FONT);
        withExpiryTable.setAutoCreateRowSorter(true);
        withExpiryTable.setDefaultRenderer(java.sql.Date.class, new DateCellRenderer());
        JScrollPane withExpiryScroll = new JScrollPane(withExpiryTable);
        JButton refreshWithExpiry = styledButton("Refresh");
        refreshWithExpiry.addActionListener(e -> refreshExpiryTable(withExpiryTable, "products_with_expiry"));
        JPanel withExpiryButtonPanel = new JPanel();
        withExpiryButtonPanel.add(refreshWithExpiry);
        withExpiryPanel.add(withExpiryScroll, BorderLayout.CENTER);
        withExpiryPanel.add(withExpiryButtonPanel, BorderLayout.SOUTH);
        innerTabbedPane.addTab("With Expiry", withExpiryPanel);

        // Products without expiry
        JPanel withoutExpiryPanel = new JPanel(new BorderLayout());
        JTable withoutExpiryTable = new JTable();
        withoutExpiryTable.setRowHeight(30);
        withoutExpiryTable.setFont(LABEL_FONT);
        withoutExpiryTable.setAutoCreateRowSorter(true);
        JScrollPane withoutExpiryScroll = new JScrollPane(withoutExpiryTable);
        JButton refreshWithoutExpiry = styledButton("Refresh");
        refreshWithoutExpiry.addActionListener(e -> refreshNonExpiryTable(withoutExpiryTable));
        JPanel withoutExpiryButtonPanel = new JPanel();
        withoutExpiryButtonPanel.add(refreshWithoutExpiry);
        withoutExpiryPanel.add(withoutExpiryScroll, BorderLayout.CENTER);
        withoutExpiryPanel.add(withoutExpiryButtonPanel, BorderLayout.SOUTH);
        innerTabbedPane.addTab("Without Expiry", withoutExpiryPanel);

        // Initial load
        refreshExpiryTable(withExpiryTable, "products_with_expiry");
        refreshNonExpiryTable(withoutExpiryTable);

        panel.add(innerTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Alerts Table
        DefaultTableModel alertsModel = new DefaultTableModel(
            new Object[]{"Product ID", "Product Name", "Expiry Date", "Days Left"}, 0);
        JTable alertsTable = new JTable(alertsModel);
        alertsTable.setRowHeight(30);
        alertsTable.setFont(LABEL_FONT);
        alertsTable.getColumnModel().getColumn(3).setCellRenderer(new DaysLeftRenderer());

        // Notifications Table
        DefaultTableModel notificationsModel = new DefaultTableModel(
            new Object[]{"Notification ID", "Message", "Timestamp"}, 0);
        JTable notificationsTable = new JTable(notificationsModel);
        notificationsTable.setRowHeight(30);
        notificationsTable.setFont(LABEL_FONT);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(alertsTable),
            new JScrollPane(notificationsTable));
        splitPane.setResizeWeight(0.5);

        JButton refreshButton = styledButton("Refresh All");
        refreshButton.addActionListener(e -> {
            refreshAlertsTable(alertsModel);
            refreshNotificationsTable(notificationsModel);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);

        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Initial load
        refreshAlertsTable(alertsModel);
        refreshNotificationsTable(notificationsModel);

        return panel;
    }

    private void handleAddProduct(JTextField... fields) {
        try {
            if (!validateFields(fields[0], fields[1], fields[2], fields[3])) return;

            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO products VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, Integer.parseInt(fields[0].getText()));
            ps.setString(2, fields[1].getText());
            ps.setDouble(3, Double.parseDouble(fields[2].getText()));
            ps.setInt(4, Integer.parseInt(fields[3].getText()));
            ps.setDate(5, fields[4].getText().isEmpty() ? 
                null : java.sql.Date.valueOf(fields[4].getText()));

            ps.executeUpdate();
            showSuccess("Product added successfully!");
            clearFields(fields);
        } catch (Exception ex) {
            showError("Add Error: " + ex.getMessage());
        }
    }

    private void handleFetchProduct(JTextField... fields) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM products WHERE product_id = ?");
            ps.setInt(1, Integer.parseInt(fields[0].getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fields[1].setText(rs.getString("product_name"));
                fields[2].setText(String.valueOf(rs.getDouble("price")));
                fields[3].setText(String.valueOf(rs.getInt("stock")));
                java.sql.Date expiry = rs.getDate("expiry_date");
                fields[4].setText(expiry != null ? expiry.toString() : "");
            } else {
                showError("Product not found!");
            }
        } catch (Exception ex) {
            showError("Fetch Error: " + ex.getMessage());
        }
    }

    private void handleUpdateProduct(JTextField... fields) {
        try {
            if (!validateFields(fields[0])) return;

            PreparedStatement ps = connection.prepareStatement(
                "UPDATE products SET product_name=?, price=?, stock=?, expiry_date=? WHERE product_id=?");
            ps.setString(1, fields[1].getText());
            ps.setDouble(2, Double.parseDouble(fields[2].getText()));
            ps.setInt(3, Integer.parseInt(fields[3].getText()));
            ps.setDate(4, fields[4].getText().isEmpty() ? 
                null : java.sql.Date.valueOf(fields[4].getText()));
            ps.setInt(5, Integer.parseInt(fields[0].getText()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                showSuccess("Product updated successfully!");
            } else {
                showError("Product not found!");
            }
        } catch (Exception ex) {
            showError("Update Error: " + ex.getMessage());
        }
    }

    private void refreshProductsTable() {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price", "Stock", "Expiry Date"}, 0) {
                @Override
                public Class<?> getColumnClass(int column) {
                    return column == 4 ? java.sql.Date.class : Object.class;
                }
            };

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getDate("expiry_date")
                });
            }
            productsTable.setModel(model);
            productsTable.repaint();
        } catch (SQLException ex) {
            showError("Load Error: " + ex.getMessage());
        }
    }

    private void refreshExpiryTable(JTable table, String viewName) {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price", "Stock", "Expiry Date"}, 0) {
                @Override
                public Class<?> getColumnClass(int column) {
                    return column == 4 ? java.sql.Date.class : Object.class;
                }
            };

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + viewName);
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getDate("expiry_date")
                });
            }
            table.setModel(model);
            table.repaint();
        } catch (SQLException ex) {
            showError("Load Error: " + ex.getMessage());
        }
    }

    private void refreshNonExpiryTable(JTable table) {
        try {
            DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price", "Stock"}, 0);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products_without_expiry");
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                });
            }
            table.setModel(model);
            table.repaint();
        } catch (SQLException ex) {
            showError("Load Error: " + ex.getMessage());
        }
    }

    private void refreshAlertsTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String query = "SELECT product_id, product_name, expiry_date, "
                         + "DATEDIFF(expiry_date, CURDATE()) AS days_left "
                         + "FROM products "
                         + "WHERE expiry_date IS NOT NULL "
                         + "AND expiry_date BETWEEN CURDATE() AND CURDATE() + INTERVAL 2 DAY "
                         + "ORDER BY expiry_date ASC";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDate("expiry_date"),
                    rs.getInt("days_left")
                });
            }
        } catch (SQLException ex) {
            showError("Alerts Error: " + ex.getMessage());
        }
    }

    private void refreshNotificationsTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM notifications ORDER BY created_at DESC");
            
            while(rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("notification_id"),
                    rs.getString("message"),
                    rs.getTimestamp("created_at")
                });
            }
        } catch (SQLException ex) {
            showError("Notifications Error: " + ex.getMessage());
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(LABEL_FONT);
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return button;
    }

    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showError("All mandatory fields (*) must be filled!");
                return false;
            }
        }
        return true;
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private static class DateCellRenderer extends DefaultTableCellRenderer {
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        public DateCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof java.sql.Date) {
                value = sdf.format((java.sql.Date) value);
            }
            return super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
        }
    }

    private static class DaysLeftRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            int daysLeft = (Integer) value;
            if(daysLeft <= 0) {
                c.setBackground(new Color(255, 200, 200));
            } else if(daysLeft == 1) {
                c.setBackground(new Color(255, 255, 200));
            } else {
                c.setBackground(table.getBackground());
            }
            
            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SupermarketManagement();
        });
    }
}