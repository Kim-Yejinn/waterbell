<template>
  <div class="each-report">
    <!-- 게시판 이름과 목록관련버튼들 -->
    <div class="report-header">
      <div class="board-title title">
        <h2>신고접수</h2>
      </div>
      <div class="list-btn">
        <button class="list-btn-list" @click="goReportList">목록</button>
        <!-- <button>윗글</button>
        <button>아랫글</button> -->
      </div>
    </div>
    <!-- 신고접수 글 -->
    <div class="report-body">
      <div class="report-title-box">
        <div class="report-title">
          <div>{{ reportInfo?.title }}</div>
        </div>
        <div class="report-info">
          <div class="report-info info-box">
            <div class="into-status">{{ reportInfo?.apartMember.name }}</div>
            <div>|</div>
            <div>{{ formattedTime(reportInfo?.createDate) }}</div>
          </div>
          <div class="report-info info-box">
            <select
              v-if="role == 'APART_MANAGER'"
              name="report-status"
              v-model="selectedStatus"
              class="custom-select"
              :class="{
                'text-primary': selectedStatus == '2',
                'text-success': selectedStatus == '1',
                'text-danger': selectedStatus == '0'
              }"
            >
              <option
                :class="{
                  'text-primary': status.value == '2',
                  'text-success': status.value == '1',
                  'text-danger': status.value == '0'
                }"
                v-for="(status, index) in statusList"
                :key="index"
                :value="status.value"
              >
                {{ status.text }}
              </option>
            </select>
            <div
              v-else
              class="info-status"
              :class="{
                'text-primary': reportInfo?.status == 'COMPLETE',
                'text-success': reportInfo?.status == 'PROCESSING',
                'text-danger': reportInfo?.status == 'BEFORE'
              }"
            >
              {{ statusEngToKr(reportInfo?.status) }}
            </div>
            <div class="viewCount">
              <div><i class="fas fa-eye"></i></div>
              <div>{{ reportInfo?.viewCount }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="report-content">
        <div class="content-text">
          {{ reportInfo?.content }}
        </div>
        <div class="content-image">
          <img
            class="report-image"
            v-for="(image, index) in imageList"
            :key="index"
            :src="image.url"
            :alt="image.imageName"
          />
        </div>
      </div>
    </div>
    <!-- 수정, 삭제 버튼 -->
    <div class="report-footer">
      <!-- 관리자 -->
      <div v-if="role == 'APART_MANAGER'" class="manager-btn">
        <button
          class="manager-btn-modify"
          @click="statusUpdate(reportInfo?.id)"
        >
          수정
        </button>
        <button class="btn-delete" @click="deleteReportManager">삭제</button>
      </div>
      <!-- 작성자 -->
      <div
        v-if="
          role == 'APART_MEMBER' && reportInfo?.apartMember.id == userInfo?.id
        "
      >
        <button class="btn-modify" @click="goToUpdate(reportInfo?.id)">
          수정
        </button>
        <button class="btn-delete" @click="deleteReport(reportInfo?.id)">
          삭제
        </button>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, defineComponent, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router'

export default defineComponent({
  name: 'parkReportItemVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id

    const role = computed(() => store.getters['auth/role']).value

    const reportInfo = ref(null) // 신고접수 글 데이터
    const imageList = ref(null) // 첨부파일 데이터

    const apiClient = axios.apiClient(store)
    const api = axios.api

    const userInfo = ref(null)

    function getUserInfo() {
      apiClient
        .get(`/member/findMember/token`)
        .then((res) => {
          userInfo.value = res.data.member
          console.log(userInfo.value)
        })
        .catch((err) => console.log(err))
    }

    // const isAuthor = ref(false) // 접속한 유저가 이 글의 작성자인지
    const reportStatus = ref<string | null>()
    // 신고접수 글 처리상태 리스트
    const statusList = [
      {
        text: '처리전',
        value: '0'
      },
      {
        text: '처리중',
        value: '1'
      },
      {
        text: '처리완료',
        value: '2'
      }
    ]
    const selectedStatus = ref('0')

    // 작성일 포맷팅
    const formattedTime = (dateTime: string) => {
      let date = new Date(dateTime)
      let year = date.getFullYear()
      let month = (1 + date.getMonth()).toString().padStart(2, '0')
      let day = date.getDate().toString().padStart(2, '0')
      let hours = date.getHours().toString().padStart(2, '0')
      let minutes = date.getMinutes().toString().padStart(2, '0')
      let seconds = date.getSeconds().toString().padStart(2, '0')

      return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}:${seconds}`
    }

    function statusEngToKr(status: string) {
      if (status === 'BEFORE') {
        return '처리전'
      } else if (status === 'PROCESSING') {
        return '처리중'
      } else if (status === 'COMPLETE') {
        return '처리완료'
      }
    }

    function getReportData() {
      apiClient
        .get(`/reports/apart/ApartBoard/detail/${report_id}`)
        .then((res) => {
          console.log(res.data)
          imageList.value = res.data.imageList
          reportInfo.value = res.data.board
          if (res.data.board.status == 'BEFORE') {
            selectedStatus.value = '0'
          } else if (res.data.board.status == 'PROCESSING') {
            selectedStatus.value = '1'
          } else {
            selectedStatus.value = '2'
          }
        })
        .catch((err) => console.log(err))
    }

    function goReportList() {
      router.push({ path: '/park/report' })
    }

    function goToUpdate(report_id: any) {
      router.push({ path: `/park/report/update/${report_id}` })
    }

    function statusNumToStr() {
      console.log(selectedStatus.value)
      if (selectedStatus.value === '0') {
        reportStatus.value = 'BEFORE'
      } else if (selectedStatus.value === '1') {
        reportStatus.value = 'PROCESSING'
      } else {
        reportStatus.value = 'COMPLETE'
      }
      return reportStatus.value
    }

    // 글 삭제 (작성자)
    function deleteReport(report_id: any) {
      api
        .get(`/reports/apart/deleteBoard/${report_id}`)
        .then(() => {
          router.push({ path: `/park/report` })
        })
        .catch((error) => {
          if (error.response.data.exception == '권한이 없습니다') {
            alert('본인이 작성한 글이 아닙니다.')
          }
        })
    }

    // 처리상태 수정 (관리자)
    async function statusUpdate(report_id: any) {
      const boardStatus = await statusNumToStr()
      console.log('처리상태 업데이트: ', boardStatus)
      apiClient
        .get(
          `/reports/apartManager/updateStatus/apartBoard/${report_id}/${boardStatus}`
        )
        .then((res) => {
          console.log(res)
          alert('처리상태가 변경되었습니다.')
        })
        .catch((err) => console.log(err))
    }
    // 글 삭제 (관리자)
    function deleteReportManager() {
      apiClient
        .get(`/reports/apart/deleteBoard/${report_id}`)
        .then((res) => {
          console.log(res)
          alert('글이 삭제되었습니다.')
          router.push({ path: '/park/report' })
        })
        .catch((err) => console.log(err))
    }

    onMounted(() => {
      getReportData()
      getUserInfo()
    })

    return {
      statusList,
      selectedStatus,
      role,
      report_id,
      reportInfo,
      reportStatus,
      imageList,
      userInfo,
      getReportData,
      goReportList,
      goToUpdate,
      statusUpdate,
      statusNumToStr,
      statusEngToKr,
      formattedTime,
      deleteReport,
      deleteReportManager,
      getUserInfo
    }
  }
})
</script>
<style scoped>
.viewCount {
  display: flex;
  width: 40px;
  gap: 5px;
}

.each-report {
  width: 100%;
  padding: 20px;
}

.each-report > div {
  padding: 10px;
}

.list-btn {
  display: flex;
  justify-content: flex-start;
}
.list-btn-list {
  display: flex;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--1, #10316b);
  color: var(--1, #10316b);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: #f2f7ff;
}

.report-header {
  border-bottom: 2px solid black;
}

.report-body {
  border-bottom: 2px solid black;
}

.report-title-box {
  border-bottom: 1px solid black;
  text-align: start;
}

.report-title-box > div {
  margin: 5px 0px;
}

.report-info {
  display: flex;
  justify-content: space-between;
  color: var(--unnamed, #939393);
  text-align: center;

  margin-top: 5px;
  font-size: 15px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 140% */
  letter-spacing: 0.25px;
}

.report-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px;
}

.info-box {
  gap: 10px;
}

.report-image {
  width: 300px;
  height: 200px;
}

.report-footer {
  display: flex;
  justify-content: flex-end;
}

.btn-modify {
  width: 101px;
  margin: 10px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  /* background: #ffa132; */
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  border: 1px solid var(--unnamed, #ffa132);
  background-color: #ffa132;
  color: #fff;
  text-align: center;
}

.btn-delete {
  margin: 10px 10px 10px 0;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--unnamed, #ffa132);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: white;
  color: var(--unnamed, #ffa132);
  text-align: center;
}

.report-title {
  color: #000;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
  letter-spacing: 0.25px;
  margin: 5px;
}

.info-status {
  color: var(--ing, #0d7e83);
}

.password-check-modal {
  gap: 10px;
  /* display: flex; */
  justify-content: center; /* 가로 가운데 정렬 */
  align-items: center; /* 세로 가운데 정렬 */
}

.password-input {
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.2);
  background: rgba(217, 217, 217, 0);
  line-height: 10px; /* 128.571% */
}

.password-check-btn {
  margin: 5px;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--1, #10316b);
  color: var(--1, #10316b);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: white;
}

.manager-btn-modify {
  width: 101px;
  margin: 10px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  /* background: #ffa132; */
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  border: 1px solid var(--unnamed, #ffa132);
  background-color: #ffa132;
  color: #fff;
  text-align: center;
}

.custom-select {
  height: 30px;
  width: 100%;
  padding: px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  appearance: none;
  cursor: pointer;
  color: var(--ing, #0d7e83);
  text-align: center;
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
}

.custom-select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.custom-select:after {
  content: '\25BC';
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  pointer-events: none;
  color: #888;
  color: var(--ing, #0d7e83);
  text-align: center;
}

/* Hover effect */
.custom-select:hover {
  border-color: #999;
}

/* Disabled state */
.custom-select:disabled {
  background-color: #f0f0f0;
  cursor: not-allowed;
}

/* Styling for options */
option {
  background-color: #fff;
  color: #333;
  color: var(--ing, #0d7e83);
  text-align: center;
}

/* Hover effect for options */
option:hover {
  background-color: #007bff;
  color: #fff;
}

.cancel-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--unnamed, #939393);
  color: #fff;
  text-align: center;
  /* Button */
  /* font-family: Roboto; */
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 142.857% */
  letter-spacing: 1.25px;
  text-transform: uppercase;
  border: 0;
}

.ok-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--1, #10316b);
  color: #fff;
  text-align: center;
  /* Button */
  /* font-family: Roboto; */
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 142.857% */
  letter-spacing: 1.25px;
  text-transform: uppercase;
  border: 0;
}

.modify-inner-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.center-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}
</style>
