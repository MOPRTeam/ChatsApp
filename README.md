# Đồ án môn học Lập trình Android
- Đề tài: **XÂY DỰNG ỨNG DỤNG NHẮN TIN TƯƠNG TỰ WHATSAPP**
- Giảng viên hướng dẫn: **TS. Huỳnh Xuân Phụng**
- Sinh viên thực hiện:
	- Hoàng Trung Nhật	. MSSV: 19110253
	- Phan Thanh Sang	. MSSV: 19110276
	- Quách Đinh Trường Thi	. MSSV: 19110294

## Hướng dẫn cài đặt 
### Yêu cầu
- IDE: Android Studio
- Runtime version: 11.0.11
- VM: OpenJDK 64-Bit Server VM by Oracle Corporation
### Lưu ý
- Các nhóm khác khi build project của nhóm mình thì sẽ không chạy được chức năng OTP, nguyên nhân là do Android Studio của 
các bạn chưa được config vào Firebase Project của tụi mình nên Firebase nó sẽ chặn chức năng OTP. Nếu các bạn muốn chạy thử ứng dụng của nhóm mình thì vui lòng cài ứng dụng bằng .apk file tụi mình đã build sẵn được đặt trong folder OUTPUT.
- Vì dịch vụ Firebase tụi mình đang dùng là miễn phí, có giới hạn OTP SMS 50 tin nhắn/ngày nên mong các bạn đừng quá spam để ứng dụng hoạt động một cách tốt nhất.

## Tài liệu liên quan
- Firebase Auth Docs: https://firebase.google.com/docs/auth/android/start
- Realtime Database Docs: https://firebase.google.com/docs/database/android/start
- Firebase Remote Config: https://firebase.google.com/docs/remote-config/get-started?platform=android
- Firebase Cloud Messaging: https://firebase.google.com/docs/cloud-messaging/android/client
- Volley Android: https://google.github.io/volley/
 
## Thư viện dùng trong project
- OTP View: https://github.com/mukeshsolanki/android-otpview-pinview
- Glide Image Processing: https://github.com/bumptech/glide
- Circular Image View: https://github.com/hdodenhof/CircleImageView
- Reactions View: https://github.com/pgreze/android-reactions
- Circular Status View: https://github.com/3llomi/CircularStatusView
- Shimmer RecyclerView: https://github.com/sharish/ShimmerRecyclerView
- Launcher Icon Generator: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html