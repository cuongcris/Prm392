package com.example.bluegreen.Domain;

import java.security.SecureRandom;

public class Utils {
    public static final String EMAIL = "hoangbig1@gmail.com";
    public static final String PASSWORD = "jxuibgdogvmixpvo";
    public static final String SUBJECT = "Verify your email";
    public static final String ADMIN_SUBJECT = "Information about new Order";
    public static final String ADMIN_Email = "nguyenhoang062017@gmail.com";

    public static final String ADMIN_Email_DisplayName ="BlueGreen Food";

    //region messageOTPForm
    public String messageOTP = "<html>" +
            "<head>" +
            "<style>" +
            "body {font-family: Arial, sans-serif; line-height: 1.6;}" +
            ".container {padding: 20px; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 10px;}" +
            "h1 {color: #4CAF50;}" +
            "p {margin: 0 0 10px;}" +
            ".otp {font-size: 20px; font-weight: bold; color: #333;}" +
            ".note {font-size: 14px; color: #888;}" +
            "hr {border: 0; border-top: 1px solid #ddd; margin: 20px 0;}" +
            ".footer {font-size: 12px; color: #888; text-align: center;}" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<h1>Dear Customer,</h1>" +
            "<p>This email is sent to confirm that we have received your request and this email address is now being verified.</p>" +
            "<p>If you have requested a change or update, please use the OTP below to complete the verification process:</p>" +
            "<p class='otp'>OTP: %s</p>" + // Placeholder for OTP
            "<p class='note'>Note: The OTP is valid for 10 minutes.</p>" +
            "<p>If you did not request this confirmation, please ignore this email or contact us for assistance.</p>" +
            "<p>Sincerely,<br>BlueGreenFood</p>" +
            "<hr>" +
            "<p>If you need further information or have any questions, do not hesitate to contact us.</p>" +
            "<p>Sincerely,<br>BlueGreenFood</p>" +
            "<div class='footer'>" +
            "<p>&copy; 2024 BlueGreenFood. All rights reserved.</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>";
    //endregion
    //region messageToAdminForm
    public String messageToAdminForm = "<html>" +
            "<body>" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\">" +
            "<tbody>" +
            "<tr>" +
            "<td align=\"center\" valign=\"top\">" +
            "<div></div>" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color:#ffffff;border:1px solid #dedede;border-radius:3px\">" +
            "<tbody>" +
            "<tr>" +
            "<td align=\"center\" valign=\"top\">" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"background-color:#96588a;color:#ffffff;border-bottom:0;font-weight:bold;line-height:100%;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;border-radius:3px 3px 0 0\">" +
            "<tbody>" +
            "<tr>" +
            "<td style=\"padding:36px 48px;display:block\">" +
            "<h1 style=\"font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:30px;font-weight:300;line-height:150%;margin:0;text-align:left;color:#ffffff;background-color:inherit\">New Order: #{{OrderCode}}</h1>" +
            "</td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</td>" +
            "</tr>" +
            "<tr>" +
            "<td align=\"center\" valign=\"top\">" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">" +
            "<tbody>" +
            "<tr>" +
            "<td valign=\"top\" style=\"background-color:#ffffff\">" +
            "<table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">" +
            "<tbody>" +
            "<tr>" +
            "<td valign=\"top\" style=\"padding:48px 48px 32px\">" +
            "<div style=\"color:#636363;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">" +
            "<p style=\"margin:0 0 16px\">You have received an order from {{CustomerName}}. The order details are as follows:</p>" +
            "<h2 style=\"color:#96588a;display:block;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:18px;font-weight:bold;line-height:130%;margin:0 0 18px;text-align:left\">" +
            "<a href=\"#\" style=\"font-weight:normal;text-decoration:underline;color:#96588a\" target=\"_blank\">[Order #{{OrderCode}}]</a> ({{OrderDate}})" +
            "</h2>" +
            "<div style=\"margin-bottom:40px\">" +
            "<table cellspacing=\"0\" cellpadding=\"6\" border=\"1\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;width:100%;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">" +
            "<thead>" +
            "<tr>" +
            "<th scope=\"col\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">Product</th>" +
            "<th scope=\"col\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">Quantity</th>" +
            "<th scope=\"col\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">Price</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "{{Products}}" +
            "</tbody>" +
            "<tfoot>" +
            "<tr>" +
            "<th scope=\"row\" colspan=\"2\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left;border-top-width:4px\">Subtotal:</th>" +
            "<td style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left;border-top-width:4px\"><span>{{Subtotal}}&nbsp;<span></span></span></td>" +
            "</tr>" +
            "<tr>" +
            "<th scope=\"row\" colspan=\"2\" style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">Total: 10% VAT, $20 SHIP</th>" +
            "<td style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\"><span>{{Total}}&nbsp;<span></span></span></td>" +
            "</tr>" +
            "</tfoot>" +
            "</table>" +
            "</div>" +
            "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;vertical-align:top;margin-bottom:40px;padding:0\">" +
            "<tbody>" +
            "<td valign=\"top\" width=\"50%\" style=\"text-align:left;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;border:0;padding:0\">" +
            "<h2 style=\"color:#96588a;display:block;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:18px;font-weight:bold;line-height:130%;margin:0 0 18px;text-align:left\">Shipping Information</h2>" +
            "<address style=\"padding:12px;color:#636363;border:1px solid #e5e5e5\">" +
            "CustomerName: {{CustomerName}}<br>ShippingAddress: {{ShippingAddress}}<br><a href=\"tel:{{Phone}}\" style=\"color:#96588a;font-weight:normal;text-decoration:underline\" target=\"_blank\">Phone: {{Phone}}</a><br><a href=\"mailto:{{Email}}\" target=\"_blank\">Email: {{Email}}</a>" +
            "</address>" +
            "</td>" +
            "</tbody>" +
            "</table>" +
            "</div>" +
            "</td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</body>" +
            "</html>";
    //endregion
    //region emailToCustomerContent
    public String emailToCustomerContent = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
            "       style=\"background-color:#ffffff;border:1px solid #dedede;border-radius:3px\">\n" +
            "    <tbody>\n" +
            "        <tr>\n" +
            "            <td align=\"center\" valign=\"top\">\n" +
            "\n" +
            "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
            "                       style=\"background-color:#96588a;color:#ffffff;border-bottom:0;font-weight:bold;line-height:100%;vertical-align:middle;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;border-radius:3px 3px 0 0\">\n" +
            "                    <tbody>\n" +
            "                        <tr>\n" +
            "                            <td style=\"padding:36px 48px;display:block\">\n" +
            "                                <h1 style=\"font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:30px;font-weight:300;line-height:150%;margin:0;text-align:left;color:#ffffff;background-color:inherit\">\n" +
            "                                    Thank you for your order\n" +
            "                                </h1>\n" +
            "                            </td>\n" +
            "                        </tr>\n" +
            "                    </tbody>\n" +
            "                </table>\n" +
            "\n" +
            "            </td>\n" +
            "        </tr>\n" +
            "        <tr>\n" +
            "            <td align=\"center\" valign=\"top\">\n" +
            "\n" +
            "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
            "                    <tbody>\n" +
            "                        <tr>\n" +
            "                            <td valign=\"top\" style=\"background-color:#ffffff\">\n" +
            "\n" +
            "                                <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
            "                                    <tbody>\n" +
            "                                        <tr>\n" +
            "                                            <td valign=\"top\" style=\"padding:48px 48px 32px\">\n" +
            "                                                <div style=\"color:#636363;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">\n" +
            "\n" +
            "                                                    <p style=\"margin:0 0 16px\">Hello {{CustomerName}},</p>\n" +
            "\n" +
            "\n" +
            "                                                    <h2 style=\"color:#96588a;display:block;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:18px;font-weight:bold;line-height:130%;margin:0 0 18px;text-align:left\">\n" +
            "                                                        [Order #{{OrderCode}}] ({{OrderDate}})\n" +
            "                                                    </h2>\n" +
            "\n" +
            "                                                    <div style=\"margin-bottom:40px\">\n" +
            "                                                        <table cellspacing=\"0\" cellpadding=\"6\" border=\"1\"\n" +
            "                                                               style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;width:100%;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif\">\n" +
            "                                                            <thead>\n" +
            "\n" +
            "                                                                <tr>\n" +
            "                                                                    <th scope=\"col\"\n" +
            "                                                                        style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">\n" +
            "                                                                        Product\n" +
            "                                                                    </th>\n" +
            "                                                                    <th scope=\"col\"\n" +
            "                                                                        style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">\n" +
            "                                                                        Quantity\n" +
            "                                                                    </th>\n" +
            "                                                                    <th scope=\"col\"\n" +
            "                                                                        style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">\n" +
            "                                                                        Price\n" +
            "                                                                    </th>\n" +
            "                                                                </tr>\n" +
            "                                                            </thead>\n" +
            "                                                            <tbody>\n" +
            "                                                                {{Products}}\n" +
            "                                                            </tbody>\n" +
            "                                                            <tfoot>\n" +
            "                                                                <tr>\n" +
            "                                                                    <th scope=\"row\" colspan=\"2\"\n" +
            "                                                                        style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left;border-top-width:4px\">\n" +
            "                                                                        Subtotal:\n" +
            "                                                                    </th>\n" +
            "                                                                    <td style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left;border-top-width:4px\">\n" +
            "                                                                        <span>{{Subtotal}}&nbsp;<span></span></span>\n" +
            "                                                                    </td>\n" +
            "                                                                </tr>\n" +
            "                                                                <tr>\n" +
            "                                                                    <th scope=\"row\" colspan=\"2\"\n" +
            "                                                                        style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">\n" +
            "                                                                        Total: 10% VAT, $20 SHIP\n" +
            "                                                                    </th>\n" +
            "                                                                    <td style=\"color:#636363;border:1px solid #e5e5e5;vertical-align:middle;padding:12px;text-align:left\">\n" +
            "                                                                        <span>{{Total}}&nbsp;<span></span></span>\n" +
            "                                                                    </td>\n" +
            "                                                                </tr>\n" +
            "                                                            </tfoot>\n" +
            "                                                        </table>\n" +
            "                                                    </div>\n" +
            "\n" +
            "\n" +
            "                                                    <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"\n" +
            "                                                           style=\"width:100%;vertical-align:top;margin-bottom:40px;padding:0\">\n" +
            "                                                        <tbody>\n" +
            "                                                            <tr>\n" +
            "                                                                <td valign=\"top\" width=\"50%\"\n" +
            "                                                                    style=\"text-align:left;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;border:0;padding:0\">\n" +
            "                                                                    <h2 style=\"color:#96588a;display:block;font-family:'Helvetica Neue',Helvetica,Roboto,Arial,sans-serif;font-size:18px;font-weight:bold;line-height:130%;margin:0 0 18px;text-align:left\">\n" +
            "                                                                        Shipping Information\n" +
            "                                                                    </h2>\n" +
            "\n" +
            "                                                                    <address style=\"padding:12px;color:#636363;border:1px solid #e5e5e5\">\n" +
            "                                                                        CustomerName: {{CustomerName}}<br>ShippingAddress: {{ShippingAddress}} <br>Phone: <a href=\"tel:{{Phone}}\"\n" +
            "                                                                                                                      style=\"color:#96588a;font-weight:normal;text-decoration:underline\"\n" +
            "                                                                                                                      target=\"_blank\">{{Phone}}</a> <br>Email: <a href=\"mailto:{{Email}}\"\n" +
            "                                                                                                                                                           target=\"_blank\">{{Email}}</a>\n" +
            "                                                                    </address>\n" +
            "                                                                </td>\n" +
            "                                                            </tr>\n" +
            "                                                        </tbody>\n" +
            "                                                    </table>\n" +
            "                                                    <p>Expected Delivery: Your order will be delivered within 3-7 days.</p>\n" +
            "                                                    <p>If you do not receive your order within this timeframe, please contact us immediately via email {{AdminEmail}}.</p>\n" +
            "                                                    <p>Please fill out <a href=\"https://docs.google.com/forms/d/e/1FAIpQLSdhwxkojylGbfH3y9Q5zdl9JqytY_150Su8HE2X_me3-6zJLg/viewform\">this form</a> to provide feedback on your shopping experience.</p>\n" +
            "                                                    <p style=\"margin:0 0 16px\">\n" +
            "                                                        We are processing your order. Thank you for shopping with us.\n" +
            "                                                    </p>\n" +
            "                                                </div>\n" +
            "                                            </td>\n" +
            "                                        </tr>\n" +
            "                                    </tbody>\n" +
            "                                </table>\n" +
            "\n" +
            "                            </td>\n" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</body>" +
            "</html>";
    //endregion

    // Getter for email content with OTP
    public String getEmailContentWithOTP(String OTP) {
        return String.format(messageOTP, OTP);
    }

    // Method to get the email content with placeholders replaced by actual data
    public String getAdminEmailContent(String OrderCode, String customerName, String orderDate, String products, String subtotal, String total, String shippingAddress, String phone, String email) {
        return messageToAdminForm
                .replace("{{OrderCode}}", OrderCode)
                .replace("{{CustomerName}}", customerName)
                .replace("{{OrderDate}}", orderDate)
                .replace("{{Products}}", products)
                .replace("{{Subtotal}}", subtotal)
                .replace("{{Total}}", total)
                .replace("{{ShippingAddress}}", shippingAddress)
                .replace("{{Phone}}", phone)
                .replace("{{Email}}", email);
    }

    public String getCustomerEmailContent(String OrderCode, String customerName, String orderDate, String products, String subtotal, String total, String shippingAddress, String phone, String email) {
        return emailToCustomerContent
                .replace("{{OrderCode}}", OrderCode)
                .replace("{{CustomerName}}", customerName)
                .replace("{{OrderDate}}", orderDate)
                .replace("{{Products}}", products)
                .replace("{{Subtotal}}", subtotal)
                .replace("{{Total}}", total)
                .replace("{{ShippingAddress}}", shippingAddress)
                .replace("{{Phone}}", phone)
                .replace("{{Email}}", email)
                .replace("{{AdminEmail}}", ADMIN_Email);
    }

    // Generates a random 6-digit OTP
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);  // generates a random 6-digit number
        return String.valueOf(otp);
    }
}
