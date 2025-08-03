variable "db_password" {
  description = "RDS root user password"
  type        = string
  sensitive   = true    // 민감한 정보임을 의미. 콘솔에 출력 되는 것을 방지
}